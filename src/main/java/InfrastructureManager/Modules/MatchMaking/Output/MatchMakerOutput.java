package InfrastructureManager.Modules.MatchMaking.Output;

import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;
import InfrastructureManager.ModuleManagement.PlatformOutput;
import InfrastructureManager.Modules.MatchMaking.Client.EdgeClient;
import InfrastructureManager.Modules.MatchMaking.Client.EdgeClientHistory;
import InfrastructureManager.Modules.MatchMaking.Exception.*;
import InfrastructureManager.Modules.MatchMaking.MatchMakingAlgorithm;
import InfrastructureManager.Modules.MatchMaking.MatchMakingModuleObject;
import InfrastructureManager.Modules.MatchMaking.MatchesList;
import InfrastructureManager.Modules.MatchMaking.Node.EdgeNode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class MatchMakerOutput extends MatchMakingModuleObject implements PlatformOutput {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ObjectMapper mapper;
    private final MatchesList sharedMatchesList;
    private final MatchMakingAlgorithm algorithm;
    private final List<EdgeNode> nodeList;
    private final List<EdgeClient> clientList;
    private final ConcurrentMap<String, Long> clientHeartBeatMap = new ConcurrentHashMap<>(); //<ClientID, HeartBeat>
    private final ConcurrentMap<String, Long> nodeHeartBeatMap = new ConcurrentHashMap<>(); //NodeID, heartbeat
//    private static final long WATCH_DOG_WAIT_TIME = 5000;


    public MatchMakerOutput(ImmutablePlatformModule module, String name, MatchMakingAlgorithm algorithm) {
        super(module, name);
        this.sharedMatchesList = this.getSharedList();
        this.algorithm = algorithm;
        this.mapper = new ObjectMapper();
        //synchronizedList because of concurrency
        this.nodeList = Collections.synchronizedList(new ArrayList<>());
        this.clientList = Collections.synchronizedList(new ArrayList<>());
        InjectableValues inject = new InjectableValues.Std()
                .addValue(ImmutablePlatformModule.class, module);
        mapper.setInjectableValues(inject);
    }

    private void assign(String thisClientID) throws MatchMakingModuleException, JsonProcessingException {
        EdgeClient thisClient = this.getClientByID(thisClientID);
        EdgeClientHistory clientHistoryInfo = thisClient.getClientHistory();

        boolean clientIsOnline = thisClient.isOnline();

        if (!clientIsOnline){
            logger.warn("[{}] is offline, unable to match",thisClientID);
            logger.info("client info: {}", thisClient);
            throw new ClientIsOfflineException("this [" + thisClientID + "] is offline");
        }
        //If thisClient has already been matched,
        if (sharedMatchesList.getMapping().containsKey(thisClientID)) {
//            String connectedNode = sharedMatchesList.getMapping().get(thisClientID);
            String connectedNode = sharedMatchesList.getClientConnectedNodeID(thisClientID);
            logger.warn("this client is already connected with {}", connectedNode);
            logger.info("client history info: \n{}", clientHistoryInfo);
            logger.info("mapping map: {}", sharedMatchesList.getMapping());
            throw new ClientAlreadyAssignedException("this [" + thisClientID + "] is already connected with " + "[" + connectedNode + "]");
        }
        //If thisClient has not been matched with any node and is online, then we match it
        if (!sharedMatchesList.getMapping().containsKey(thisClientID) || clientIsOnline){

            //match client with node in nodeList according to algorithm
            EdgeNode thisNode = this.algorithm.match(thisClient, nodeList);
            String thisNodeAsString = mapper.writeValueAsString(thisNode);
            String thisNodeID = thisNode.getId();

            //list of node, for debugging purposes
            nodeList.forEach(n -> logger.debug("{}", n));

            logger.info("assigned node info: \n{}", thisNode);
            logger.info("client info: \n{}", thisClient);
            logger.info("-------------- assign client [{}] to node [{}] --------------", thisClientID, thisNodeID);

            //update node's data
            updateAfterAssigning(thisClientID, thisNodeID);

            //mapping should take only client ID, because of object referencing
            //add whole JSON body of node. bao
            sharedMatchesList.putValue(thisClientID, thisNodeAsString);
            logger.info("client history info: \n{}", clientHistoryInfo);
            logger.info("mapping map: {}", sharedMatchesList);
        }
    }

    /**
     * Update node's data after assigning.
     * We minus resourced occupied by client
     *
     * @param thisClientID client ID, in String
     * @param thisNodeID   node ID, in String
     * @author Zero
     */
    private void updateAfterAssigning(String thisClientID, String thisNodeID) throws InfrastructureException {

        EdgeClient thisClient = getClientByID(thisClientID);
        EdgeNode thisNode = getNodeByID(thisNodeID);

        //Get client used resource
        long usedResource = thisClient.getReqResource();
        long usedNetwork = thisClient.getReqNetwork();

        //Get node location in list to replace later on
        int nodeLocationInList = this.nodeList.indexOf(thisNode);
        //node that we going to replace in our nodeList
        EdgeNode updateNode = new EdgeNode(this.getOwnerModule(), thisNodeID, thisNode.getIpAddress(),
                thisNode.isConnected(), thisNode.getTotalResource(), thisNode.getTotalNetwork(), thisNode.getLocation(),
                thisNode.getHeartBeatInterval(),thisNode.isOnline(),thisNode.isWatchDogOnline());

        //accounting usedResource (resource - usedResource)
        updateNode.updateComputingResource(usedResource);
        updateNode.updateNetworkBandwidth(usedNetwork);

        logger.info("Node [{}] before assigned to client [{}]: \n{}", thisNodeID, thisClientID, this.nodeList.get(nodeLocationInList));
        this.nodeList.set(nodeLocationInList, updateNode);
        logger.info("Node [{}] after assigned to client [{}]: \n{}", thisNodeID, thisClientID, this.nodeList.get(nodeLocationInList));
    }

    private EdgeClient getClientByID(String clientID) throws NoClientFoundException {
        return this.clientList.stream().filter(c -> c.getId().equals(clientID)).findFirst()
                .orElseThrow(() -> new NoClientFoundException("can not find this [" + clientID + "] in the list"));
    }

    private EdgeNode getNodeByID(String nodeID) throws NoNodeFoundException {
        return this.nodeList.stream().filter(n -> n.getId().equals(nodeID)).findFirst()
                .orElseThrow(() -> new NoNodeFoundException("can not find this [" + nodeID + "] in the list"));
    }

    /**
     * register client to the clientList if this client is not register in the system
     * Update the client's info in the list if this client already registered
     *
     * @param clientAsString JSON body of client when updating
     */
       /*
    -----------------------register client to a list--------------------------------------------------
     */
    private void registerClient(String clientAsString) throws NoClientFoundException, ClientAlreadyAssignedException, JsonProcessingException {
        logger.info("RegisterClient - client as string : {} ", clientAsString);
        //Map the contents of the JSON file to a java object
        EdgeClient thisClient = this.mapper.readValue(clientAsString, EdgeClient.class);
        String thisClientID = thisClient.getId();

        //Check whether client is already register or not
        //If not -> add client to the list, else update client info
        if (!clientList.contains(thisClient)) {
            logger.info("client after readValue from mapper: {}", thisClient);
            this.clientList.add(thisClient);
        }
        //Update client info if this client is already in the list
        else {
            //No update client that already been mapped to a node
            if (sharedMatchesList.clientIsConnected(thisClientID)) {
//                String mappedNode = sharedMatchesList.getMapping().get(thisClientID);
                String mappedNode = sharedMatchesList.getClientConnectedNodeID(thisClientID);
                throw new ClientAlreadyAssignedException("can't update because [" + thisClientID + "] already been assigned to [" + mappedNode + "]");
            }
            logger.info("this client has already been registered, updating {} stats", thisClientID);
            updateClient(clientAsString);
        }

        //Initiating client history
        EdgeClientHistory thisClientHistory = new EdgeClientHistory(this.getOwnerModule(), thisClientID);

        for (EdgeNode thisNode : this.nodeList) {
            String thisNodeID = thisNode.getId();
            thisClientHistory.setHistoryScore(thisNodeID, 0L);
            thisClientHistory.setLastConnectedTime(thisNodeID, 0L);
        }

        //assign the history above to this client's history
        thisClient.setClientHistory(thisClientHistory);

        //Put the client with its history in a HashMap, we can always update the history later on
        // by fetching thisClientHistory stuff anyway
        logger.info("this client history info: \n{}", thisClient.getClientHistory());
    }

    private boolean isClientInList (String thisClientID){

        for ( EdgeClient client : clientList){
            String clientID = client.getId();
            if (clientID.equals(thisClientID)){
                return true;
            }
        }
        return false;
    }

    private boolean isNodeInList ( String thisNodeID){
        for (EdgeNode node : nodeList){
            String nodeID = node.getId();
            if (nodeID.equals(thisNodeID)){
                return true;
            }
        }
        return false;
    }


    private void updateClient(String clientAsString) throws NoClientFoundException, JsonProcessingException {
        //Map the contents of the JSON file to a java object
        EdgeClient newClient = this.mapper.readValue(clientAsString, EdgeClient.class);
        //get updating client location
        EdgeClient oldClient = getClientByID(newClient.getId());
        logger.info("Before update {}",oldClient);
        //Reassign old value if there aren't any new value (default value = Long.MAX_VALUE when unassigned)
        if (newClient.getReqResource() == Long.MAX_VALUE) {
            logger.debug("there are no new required computing resource to update, reuse the old one");
        } else {
            oldClient.setReqResource(newClient.getReqResource());
        }

        if (newClient.getReqNetwork() == Long.MAX_VALUE) {
            logger.debug("there are no new required network bandwidth address to update, reuse the old one");
        } else {
            oldClient.setReqNetwork(newClient.getReqNetwork());
        }

        if (newClient.getLocation() == Long.MAX_VALUE) {
            logger.debug("there are no new location to update, reuse the old one");
        } else {
            oldClient.setLocation(newClient.getLocation());
        }

        if (newClient.getHeartBeatInterval() != 0){
            oldClient.setHeartBeatInterval(newClient.getHeartBeatInterval());
        }
        logger.info("After update {}",oldClient);
    }


    /**
     * register node to the nodeList if this node is not register in the system
     * Update the node's info in the list if this client already registered
     *
     * @param nodeAsString JSON body of node as String
     */
    /*
    -----------------------register node to a list--------------------------------------------------
     */
    private void registerNode(String nodeAsString) throws NoClientFoundException, NoNodeFoundException, JsonProcessingException {
        logger.info("RegisterNode - node as string: {} ", nodeAsString);
        EdgeNode thisNode = this.mapper.readValue(nodeAsString, EdgeNode.class);
        //Check whether node is already register or not
        //If not -> add node to the list, else node client info
        if (!nodeList.contains(thisNode)) {
            logger.info("node after readValue from mapper: {}", thisNode);
            this.nodeList.add(thisNode);
        }
        //Update node info if this client is already in the list
        else {
            logger.info("this node has already been registered, updating {} stats", thisNode.getId());
            updateNode(nodeAsString);
        }
    }


    /**
     * Update the node with new info/data from REST input (JSON body)
     *
     * @param nodeAsString JSON string of updating node
     * @throws NoNodeFoundException    If the required node cannot be found in the node list
     * @throws NoClientFoundException  If the node is connected but the client cannot be found in the client list
     * @throws JsonProcessingException If there is an error while parsing the node object from the JSON body
     */
    private void updateNode(String nodeAsString) throws NoNodeFoundException, NoClientFoundException, JsonProcessingException {
        logger.info("UpdateNode - node as string: {} ", nodeAsString);
        //Map the contents of the JSON file to a java object
        EdgeNode newNode = this.mapper.readValue(nodeAsString, EdgeNode.class);
        String newNodeID = newNode.getId();

        //Get updating node location in list
        int thisNodeLocation = nodeList.indexOf(getNodeByID(newNodeID));
        EdgeNode oldNode = nodeList.get(thisNodeLocation);
        logger.info("[{}] before update\n{}", oldNode.getId(), nodeList.get(thisNodeLocation));

        //Check whether node is assigned, if yes then we will deduct the accumulated network and resource when updating
        boolean nodeIsAssigned = sharedMatchesList.nodeIsAssigned(nodeAsString);
        long usedResource = 0;
        long usedNetwork = 0;
        if (nodeIsAssigned) {
            for (String client : sharedMatchesList.getConnectedClientsToNode(newNodeID)) {
                EdgeClient thisClient = getClientByID(client);
                usedNetwork = usedNetwork + thisClient.getReqNetwork();
                usedResource = usedResource + thisClient.getReqResource();
            }
            logger.info("usedNetwork : {}         usedResource : {}", usedNetwork, usedResource);
        }

        //Assign new value.
        if (newNode.getTotalResource() != 0) {
            oldNode.setTotalResource(newNode.getTotalResource());
        }
        if (newNode.getTotalNetwork() != 0) {
            oldNode.setTotalNetwork(newNode.getTotalNetwork());
        }
        if (newNode.getLocation() != 0) {
            oldNode.setLocation(newNode.getLocation());
        }
        if (!(newNode.getIpAddress().equalsIgnoreCase("null"))){
            oldNode.setIpAddress(newNode.getIpAddress());
        }
        if (newNode.getHeartBeatInterval() != 0){
            oldNode.setHeartBeatInterval(newNode.getHeartBeatInterval());
        }
        //minus the occupied resource from assigned clients
        oldNode.updateNetworkBandwidth(usedNetwork);
        oldNode.updateComputingResource(usedResource);

        logger.info("[{}] after update\n{}", oldNode.getId(), nodeList.get(thisNodeLocation));

    }

    /**
     * Update node's resources after disconnect with client (resource got freed up)
     * We recalculate resource occupied by client(s) to node.
     * Remove node and client coupling in mapper
     *
     * @param clientAsString JSON body of client when updating
     * @author Zero
     */
    private void updateAfterDisconnecting(String clientAsString) throws JsonProcessingException, MatchMakingModuleException {
        //get client ID and disconnect reason:
        //we only need the ID and message of this object anyway
        EdgeClient pseudoClient = this.mapper.readValue(clientAsString, EdgeClient.class);
        String message = pseudoClient.getMessage();
        String thisClientID = pseudoClient.getId();
        long disconnectedTime = System.currentTimeMillis();

        //this is the one that registered in the list
        EdgeClient thisClient = getClientByID(thisClientID);
        logger.info("disconnected client [{}] : \n{}", thisClientID, thisClient);


        if (!sharedMatchesList.clientIsConnected(thisClientID)) {
            logger.error("This client [{}] is not connected to any nodes in the system", thisClientID);
            throw new ClientNotAssignedException("This client [" + thisClientID + "] is not connected to any nodes in the system");
        }
        //Get node that assigned to client
        String thisNodeID = sharedMatchesList.getClientConnectedNodeID(thisClientID);
        EdgeNode thisNode = getNodeByID(thisNodeID);

        logger.info("Node [{}] before disconnect from client [{}]: \n{}", thisNodeID, thisClientID, thisNode);

        //disconnecting client with node
        sharedMatchesList.removeClient(thisClientID);
            /*
            -----------------------------UPDATING NODE INFO----------------------------------------------------------
             */
        //If node is still assigned to other client
        boolean nodeIsAssigned = sharedMatchesList.nodeIsAssigned(thisNodeID);
        long usedResource = 0;
        long usedNetwork = 0;

        //Then calculate the accumulated resource & network by other clients
        if (nodeIsAssigned) {
            List<String> assignedClients = sharedMatchesList.getConnectedClientsToNode(thisNodeID);
            for (String client : assignedClients) {
                thisClient = getClientByID(client);
                usedNetwork = usedNetwork + thisClient.getReqNetwork();
                usedResource = usedResource + thisClient.getReqResource();
            }
        }

        //Reset resource and network values
        thisNode.setTotalResource(thisNode.getTotalResource());
        thisNode.setTotalNetwork(thisNode.getTotalNetwork());

        //Update resource and network values
        thisNode.updateNetworkBandwidth(usedNetwork);
        thisNode.updateComputingResource(usedResource);
        logger.info("Node [{}] after disconnect from client [{}]: \n{}", thisNodeID, thisClientID, thisNode);

        /*
        -----------------------------HISTORY UPDATE--------------------------------------------------------------
        */
        EdgeClientHistory thisClientHistoryInfo = thisClient.getClientHistory();

        //Get score between thisClientID and thisNodeID
        Long historyScore = thisClientHistoryInfo.getHistoryScore(thisNodeID);

        //UPDATE HERE WHEN WE HAVE MORE MESSAGE
        switch (message) {
            case "job_done" -> historyScore = historyScore - 5;
            case "job_failed" -> historyScore = historyScore + 10;
        }
        if (historyScore < 0) {
            historyScore = 0L;
        }

        logger.info("History before disconnecting: \n{}", thisClientHistoryInfo);

        //Put the new history score in history info package
        thisClientHistoryInfo.setHistoryScore(thisNodeID, historyScore);
        //Add client's disconnected time to history info package
        thisClientHistoryInfo.setLastConnectedTime(thisNodeID, disconnectedTime);

        //Print out for debugging purposes:
        logger.info("History after disconnecting: \n{}", thisClientHistoryInfo);
    }

    private void disconnectAllClientsFromNode (String thisNodeID) throws NoNodeFoundException, ClientNotAssignedException, NoNodeFoundInHistoryException, NoClientFoundException {
        //check whether thisNode is matched with any clients, if yes then disconnect all clients
        logger.info("disconnecting all clients of [{}]", thisNodeID);
        if (sharedMatchesList.nodeIsAssigned(thisNodeID)){
            List<String> connectedClientsList = sharedMatchesList.getConnectedClientsToNode(thisNodeID);
            for ( String connectedClient : connectedClientsList ){
                disconnectAfterTimeOut(connectedClient,thisNodeID,"job_something");
            }
        }
    }

    /**
     * Simple disconnect function without going through POST command
     *
     */
    private void disconnectAfterTimeOut (String thisClientID, String thisNodeID, String message) throws NoClientFoundException, NoNodeFoundException, ClientNotAssignedException, NoNodeFoundInHistoryException {

        EdgeClient thisClient = getClientByID(thisClientID);
        EdgeNode thisNode = getNodeByID(thisNodeID);
        long disconnectedTime = System.currentTimeMillis();

        logger.info("disconnected client [{}] : \n{}", thisClientID, thisClient);

        if (!sharedMatchesList.clientIsConnected(thisClientID)) {
            logger.error("This client [{}] is not connected to any nodes in the system", thisClientID);
            throw new ClientNotAssignedException("This client [" + thisClientID + "] is not connected to any nodes in the system");
        }

        sharedMatchesList.removeClient(thisClientID);
        logger.info("Node [{}] before disconnect from client [{}]: \n{}", thisNodeID, thisClientID, thisNode);

         /*
            -----------------------------UPDATING NODE INFO----------------------------------------------------------
             */
        //If node is still assigned to other client
        boolean nodeIsAssigned = sharedMatchesList.nodeIsAssigned(thisNodeID);
        long usedResource = 0;
        long usedNetwork = 0;

        //Then calculate the accumulated resource & network by other clients
        if (nodeIsAssigned) {
            List<String> assignedClients = sharedMatchesList.getConnectedClientsToNode(thisNodeID);
            for (String client : assignedClients) {
                thisClient = getClientByID(client);
                usedNetwork = usedNetwork + thisClient.getReqNetwork();
                usedResource = usedResource + thisClient.getReqResource();
            }
        }

        //Reset resource and network values
        thisNode.setTotalResource(thisNode.getTotalResource());
        thisNode.setTotalNetwork(thisNode.getTotalNetwork());

        //Update resource and network values
        thisNode.updateNetworkBandwidth(usedNetwork);
        thisNode.updateComputingResource(usedResource);
        logger.info("Node [{}] after disconnect from client [{}]: \n{}", thisNodeID, thisClientID, thisNode);

        /*
        -----------------------------HISTORY UPDATE--------------------------------------------------------------
        */
        EdgeClientHistory thisClientHistoryInfo = thisClient.getClientHistory();

        //Get score between thisClientID and thisNodeID
        Long historyScore = thisClientHistoryInfo.getHistoryScore(thisNodeID);

        //UPDATE HERE WHEN WE HAVE MORE MESSAGE
        switch (message) {
            case "job_done" -> historyScore = historyScore - 5;
            case "job_failed" -> historyScore = historyScore + 10;
        }
        if (historyScore < 0) {
            historyScore = 0L;
        }

        logger.info("History before disconnecting: \n{}", thisClientHistoryInfo);

        //Put the new history score in history info package
        thisClientHistoryInfo.setHistoryScore(thisNodeID, historyScore);
        //Add client's disconnected time to history info package
        thisClientHistoryInfo.setLastConnectedTime(thisNodeID, disconnectedTime);

        //Print out for debugging purposes:
        logger.info("History after disconnecting: \n{}", thisClientHistoryInfo);
        logger.info("Disconnected this client [{}] from [{}] ", thisClientID,thisNodeID);

    }


    /**
     * create HeartBeat signal for client
     *
     * @param clientAsString JSON body of client to its heartbeat signal
     */
    private void createHeartBeatForClient(String clientAsString) throws JsonProcessingException, NoClientFoundException, WatchdogAlreadyCreated {

        EdgeClient thisClientFromString = this.mapper.readValue(clientAsString, EdgeClient.class);
        String thisClientFromStringID = thisClientFromString.getId();

        EdgeClient thisClient = getClientByID(thisClientFromStringID);

        String thisClientID = thisClient.getId();
        thisClient.setOnline(true);
        logger.info("client's INFO: {}\n,onl : {}\nwatchDog : {} ",thisClient, thisClient.isOnline(), thisClient.isWatchDogOnline());

        long heartBeatInterval = thisClient.getHeartBeatInterval();
        long createdTime = System.currentTimeMillis();
        clientHeartBeatMap.put(thisClientID,createdTime);
        final boolean[] abortCondition = {false};

        if(thisClient.isWatchDogOnline()){
            thisClient.setWatchDogOnline(true);
            logger.warn("watch dog is already online, not gonna create a new one");
        }

        Runnable r = new Runnable() {
            @Override
            public void run() {
                logger.info("watch dog for [{}] created. ",thisClientID);
                while (!abortCondition[0]) {
                    //check for heartbeat signal
                    Long currentHeartBeatTime = clientHeartBeatMap.get(thisClientID);
                    Timestamp currentHeartBeatTimeStamp = new Timestamp(currentHeartBeatTime);
                    logger.info("{} : current heart beat arrival time for [{}]",currentHeartBeatTimeStamp,thisClientID);
                    logger.info("waiting for the next heart beat (interval = {} ms) ...",heartBeatInterval);
                    long WATCH_DOG_WAIT_TIME = heartBeatInterval/3;
                    //Wait for a set period
                    try {
                        Thread.sleep(WATCH_DOG_WAIT_TIME);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    long clientHeartBeatArrivalTime = clientHeartBeatMap.get(thisClientID);
                    long deadlineTime = clientHeartBeatArrivalTime + heartBeatInterval;
                    long checkPointTime = System.currentTimeMillis();
                    boolean checkHeartBeat = doWeStillHaveTime(deadlineTime,checkPointTime);
                    Timestamp clientHeartBeatStartTimeStamp = new Timestamp(clientHeartBeatArrivalTime);
                    Timestamp deadlineTimeStamp = new Timestamp(deadlineTime);
                    Timestamp checkPointTimeStamp = new Timestamp (checkPointTime);
                    logger.info("\n{} : heartbeat arrival time\n{} : heartBeat dead line\n{} : current time",clientHeartBeatStartTimeStamp,deadlineTimeStamp,checkPointTimeStamp);

                    //reset heart beat and keep watching
                    if (checkHeartBeat) {
                        logger.info("heart beat signal = [{}], resetting watch dog",checkHeartBeat);
                        thisClient.setOnline(true);
                    }
                    //time out, abort this , tell the platform that client is offline
                    if (!checkHeartBeat) {
                        logger.info("Heart beat signal time out, [{}] status switched to [offline]",thisClientID);
                        thisClient.setOnline(false);
                        thisClient.setWatchDogOnline(false);
                        abortCondition[0] = true;
                        //if client is matched with a node in the platform, disconnect client from the node
                        if (sharedMatchesList.clientIsConnected(thisClientID)) {

                            String mappedNodeID = sharedMatchesList.getClientConnectedNodeID(thisClientID);

                            try {
                                disconnectAfterTimeOut(thisClientID,mappedNodeID,"timeout");
                            } catch (NoClientFoundException | NoNodeFoundException
                                    | ClientNotAssignedException | NoNodeFoundInHistoryException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                logger.info("watch dog for [{}] closed.",thisClientID);
            }
        };

        if(!thisClient.isWatchDogOnline()){
            thisClient.setWatchDogOnline(true);
            Thread watchDog = new Thread(r);
            watchDog.start();
        }

    }

    private void clientHeartBeatSignalReceived(String heartBeatSignalAsString) throws JsonProcessingException, NoClientFoundException, WatchdogAlreadyCreated {
        EdgeClient thisClient = this.mapper.readValue(heartBeatSignalAsString, EdgeClient.class);
        String thisClientID = thisClient.getId();

        //Check whether this client is registered or not
        if (isClientInList(thisClientID)){
            //Set client's status to [online]
            EdgeClient clientInList = getClientByID(thisClientID);
            //create a new watchdog for heartbeat signal if watchdog timed out
            if (!clientInList.isWatchDogOnline()){
                createHeartBeatForClient(heartBeatSignalAsString);
            }
            clientInList.setOnline(true);
            //Put the start time in the map
            long startTime = System.currentTimeMillis();
            Timestamp startTimeTimeStamp = new Timestamp(startTime);
            clientHeartBeatMap.put(thisClientID,startTime);
            logger.info("{}, heart beat received time", startTimeTimeStamp );
            logger.info("heart beat signal for [{}] received",thisClientID);
            //create watchDog if the previous one is gone
            if (!clientInList.isWatchDogOnline()){
                logger.info("watch dog for [{}] was offline, creating a new one",thisClientID);
                createHeartBeatForClient(heartBeatSignalAsString);
            }
        }
        else if (!isClientInList(thisClientID)){
            logger.warn("[{}] is not registered, please register client before sending heartbeat", thisClientID);
            throw new NoClientFoundException(thisClientID + " is not registered, please register client before sending heartbeat");
        }

    }



    private void nodeHeartBeatSignalReceived (String heartBeatSignalAsString) throws JsonProcessingException, NoNodeFoundException, NoClientFoundException, WatchdogAlreadyCreated {
        EdgeNode thisNode = this.mapper.readValue(heartBeatSignalAsString, EdgeNode.class);
        String thisNodeID = thisNode.getId();
        //Put the start time in the map
        if (isNodeInList(thisNodeID)){
            //set node's status to [online]
            EdgeNode nodeInList = getNodeByID(thisNodeID);
            nodeInList.setOnline(true);

            //put start time in the map
            long startTime = System.currentTimeMillis();
            Timestamp startTimeTimeStamp = new Timestamp(startTime);
            nodeHeartBeatMap.put(thisNodeID, startTime);
            logger.info("{}, heart beat received time", startTimeTimeStamp );
            logger.info("heart beat signal for [{}] received", thisNodeID);
            //create watchDog if the previous one is gone
            if(!nodeInList.isWatchDogOnline())
            {
                logger.info("watch dog was offline, creating a new one");
                createHeartBeatForNode(heartBeatSignalAsString);
            }
        }
        else if (!isNodeInList(thisNodeID)){
            logger.warn("[{}] is not registered, please register node before sending heartbeat", thisNodeID);
            throw new NoNodeFoundException( thisNodeID+ " is not registered, please register client before sending heartbeat");
        }

    }

    private boolean doWeStillHaveTime (long deadlineTime, long currentTime){
        //when we still have enough time
        if (deadlineTime >= currentTime){
            return true;
        }

        return false;
    }

    /**
     * create HeartBeat signal for node
     *
     * @param nodeAsString JSON body of node to its heartbeat signal
     */
    private void createHeartBeatForNode(String nodeAsString) throws JsonProcessingException, NoNodeFoundException {

        EdgeNode thisNodeFromString = this.mapper.readValue(nodeAsString, EdgeNode.class);
        String thisNodeFromStringID = thisNodeFromString.getId();
        EdgeNode thisNode = getNodeByID(thisNodeFromStringID);
        String thisNodeID = thisNode.getId();
        thisNode.setOnline(true);
        long heartBeatInterval = thisNode.getHeartBeatInterval();
        long createdTime = System.currentTimeMillis();
        nodeHeartBeatMap.put(thisNodeID,createdTime);

        final boolean[] abortCondition = {false};

        if(thisNode.isWatchDogOnline()){
            thisNode.setWatchDogOnline(true);
            logger.warn("watch dog is already online, not gonna create a new one");
        }

        Runnable r = new Runnable() {
            @Override
            public void run() {
                logger.info("watch dog for [{}] created. ",thisNodeID);
                thisNode.setWatchDogOnline(true);
                while (!abortCondition[0]) {
                    //get deadline for next heartbeat
                    Long currentHeartBeatTime = nodeHeartBeatMap.get(thisNodeID);
                    Timestamp currentHeartBeatTimeStamp = new Timestamp(currentHeartBeatTime);
                    logger.info("{} : current heart beat arrival time for [{}]",currentHeartBeatTimeStamp,thisNodeID);
                    logger.info("waiting for the next heart beat (interval = {} ms) ...",heartBeatInterval);
                    long WATCH_DOG_WAIT_TIME = heartBeatInterval/3;
                    //Wait for the heart beat interval
                    try {
                        Thread.sleep(WATCH_DOG_WAIT_TIME);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Long nodeHeartBeatArrivalTime = nodeHeartBeatMap.get(thisNodeID);
                    long deadlineTime = nodeHeartBeatArrivalTime + heartBeatInterval;
                    long checkPointTime = System.currentTimeMillis();
                    boolean checkHeartBeat = doWeStillHaveTime(deadlineTime,checkPointTime);
                    Timestamp nodeHeartBeatStartTimeStamp = new Timestamp(nodeHeartBeatArrivalTime);
                    Timestamp deadlineTimeStamp = new Timestamp(deadlineTime);
                    Timestamp checkPointTimeStamp = new Timestamp (checkPointTime);
                    logger.info("\n{} : heartbeat arrival time\n{} : heartBeat dead line\n{} : current time",nodeHeartBeatStartTimeStamp,deadlineTimeStamp,checkPointTimeStamp);

                    //reset heart beat and keep watching
                    if (checkHeartBeat) {
                        logger.info("heart beat signal did arrived for [{}], resetting watch dog",thisNodeID);
                        thisNode.setOnline(true);
                    }
                    //time out, abort this , tell the platform that client is offline
                    if (!checkHeartBeat) {
                        logger.info("Heart beat signal time out, [{}] status switched to [offline]",thisNodeID);
                        thisNode.setOnline(false);
                        thisNode.setWatchDogOnline(false);
                        abortCondition[0] = true;

                        //disconnect all clients that matched to thisNode
                        try {
                            disconnectAllClientsFromNode(thisNodeID);
                        } catch (NoNodeFoundException | NoNodeFoundInHistoryException | ClientNotAssignedException | NoClientFoundException e) {
                            e.printStackTrace();
                        }

                    }
                }
                logger.info("watch dog for [{}] closed.",thisNodeID);
            }
        };

        if (!thisNode.isWatchDogOnline()){
            thisNode.setWatchDogOnline(true);
            Thread watchDog = new Thread(r);
            watchDog.start();
        }
    }


    public List<EdgeNode> getNodeList() {
        return nodeList;
    }

    public List<EdgeClient> getClientList() {
        return clientList;
    }

    @Override
    public void execute(String response) throws MatchMakingModuleException {
        String[] commandLine = response.split(" ");
        if (commandLine[0].equals("matchMaker")) {
            try {
                switch (commandLine[1]) {
                    case "register_client" -> {
                        //register client and create its corresponding heartbeat
                        registerClient(commandLine[2]);
                        createHeartBeatForClient(commandLine[2]);
                        logger.info("client registered, done with [outfunction]\n---------------------------------------------------------------------------------------\n");
                    }
                    case "register_node" -> {
                        registerNode(commandLine[2]);
                        createHeartBeatForNode(commandLine[2]);
                        logger.info("node registered, done with [outfunction]\n---------------------------------------------------------------------------------------\n");
                    }
                    case "assign_client" -> {
                        assign(commandLine[2]);
                        logger.info("client assigned, done with [outfunction]\n---------------------------------------------------------------------------------------\n");
                    }
                    case "disconnect_client" -> {
                        updateAfterDisconnecting(commandLine[2]);
                        logger.info("client disconnected, done with [outfunction]\n---------------------------------------------------------------------------------------\n");
                    }
                    case "client_receive_heart_beat" -> {
                        clientHeartBeatSignalReceived(commandLine[2]);
                        logger.info("client heart beat received, done with [outfunction]\n---------------------------------------------------------------------------------------\n");
                    }
                    case "node_receive_heart_beat" -> {
                        nodeHeartBeatSignalReceived(commandLine[2]);
                        logger.info("node heart beat received, done with [outfunction]\n---------------------------------------------------------------------------------------\n");
                    }


                    default -> throw new MatchMakingModuleException("Invalid command " + commandLine[1]
                            + " for MatchMaker");
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new MatchMakingModuleException("Arguments missing for command " + response
                        + " for MatchMakerOutput");
            } catch (JsonProcessingException e) {
                throw new MatchMakingModuleException("Error processing JSON data", e);
            }
        }
    }
}
