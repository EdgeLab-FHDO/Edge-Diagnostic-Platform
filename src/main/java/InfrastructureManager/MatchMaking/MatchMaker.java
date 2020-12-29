package InfrastructureManager.MatchMaking;

import InfrastructureManager.*;
import InfrastructureManager.EdgeClientHistory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


public class MatchMaker extends MasterOutput implements MasterInput {
    /**
     * When implement new function. There are 3 things we need to do.
     * <p>
     * > Update RestInput to feed in the command for the out function in MatchMaker (here)
     * > Update RestRouter to make sure out POST/PUT/GET command from Postman will go through
     * > Update Configuration so we can accept the command from Gradle
     * <p>
     * optional: update master to match with RestInput/MatchMaker class
     * <p>
     * <p>
     * We must register all nodes before register client(s). Or else history calculate thingy will got null error
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private MatchMakingAlgorithm algorithm;
    private final ObjectMapper mapper;
    private String command = "";

    private final List<EdgeNode> nodeList;
    private final List<EdgeClient> clientList;
    //map - clientID, nodeID
    private final HashMap<String, String> mapping;

    //A hashmap with client and its history. Used to fetch history related data
    private final HashMap<String, EdgeClientHistory> clientHistoryHashMap = new HashMap<>();


    /*
    -----------------------start_match_m start from here--------------------------------------------------
     */
    public MatchMaker(String name, String algorithmType) {
        super(name);

        System.out.println("input for match making:  \nstart_rest_server \nstart_runner Runner_rest_in \nstart_runner Runner_match_m \n");
        setAlgorithmFromString(algorithmType);
        //initiate variables
        this.mapper = new ObjectMapper();
        this.nodeList = new ArrayList<>();
        this.clientList = new ArrayList<>();
        this.mapping = new HashMap<>();
    }


    /*
    -----------------------getting match making algorithm--------------------------------------------------
     */
    public void setAlgorithmFromString(String algorithmType) {
        logger.info("Algorithm used: {}", algorithmType);
        switch (algorithmType.toLowerCase()) {
            case "random" -> setAlgorithm(new RandomMatchMaking());
            case "score" -> setAlgorithm(new ScoreBasedMatchMaking());
            case "naive" -> setAlgorithm(new NaiveMatchMaking());
            default -> throw new IllegalArgumentException("Invalid type for MatchMaker");
        }
    }

    private void setAlgorithm(MatchMakingAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    /*
    -----------------------Assigning client to node--------------------------------------------------
    MATCH MAKING ACTION, single client to single node, not multiple in the same time.
    */
    private void assign(String thisClientID) {

        try {
            EdgeClient thisClient = this.getClientByID(thisClientID);
            EdgeClientHistory clientHistoryInfo = thisClient.getClientHistory();
            //Check whether thisClient has already been matched or not

            if (mapping.containsKey(thisClientID)) {
                String connectedNode = mapping.get(thisClientID);
                logger.warn("this client is already connected with {}", connectedNode);
                logger.info("client history info: \n{}", clientHistoryInfo);
                logger.info("mapping map: {}", mapping);
                throw new Exception("this [" + thisClientID + "] is already connected with " + connectedNode);
            } else {
                //match client with node in nodelist according to algorithm
                EdgeNode thisNode = this.algorithm.match(thisClient, this.nodeList, clientHistoryInfo);
                String thisNodeID = thisNode.getId();
                //list of node, for debugging purposes
                for (EdgeNode theNode : nodeList) {
                    logger.info("{}", theNode);
                }
                logger.info("assigned node info: \n{}", thisNode);
                logger.info("client info: \n{}", thisClient);
                logger.info("assign client [{}] to node [{}] ", thisClientID, thisNodeID);
                //DONE: update node's data
                if (!thisNode.getId().equals("rejectNode")) {
                    updateAfterAssigning(thisClientID, thisNodeID);
                    command = "give_node " + thisClientID + " " + thisNodeID;
                } else {
                    //no out command if we couldn't assign the client to any node. No need for further operation here
                    //TODO: maybe send a rejected response if this happen
                    command = "";
                }
                //DONE: mapping should take only client ID, because of object referencing
                this.mapping.put(thisClientID, thisNodeID);
                logger.info("client history info: \n{}", clientHistoryInfo);
                logger.info("mapping map: {}", mapping);
            }
        } catch (
                Exception e) {
            logger.error("error in assigning client");
            e.printStackTrace();
        }

    }

    //DONE: update node data after disconnecting, remove it from mapper

    /**
     * Update node's data after assigning.
     * We minus resourced occupied by client
     *
     * @param thisClientID client ID, in String
     * @param thisNodeID   node ID, in String
     * @author Zero
     */
    private void updateAfterAssigning(String thisClientID, String thisNodeID) throws Exception {

        EdgeClient thisClient = getClientByID(thisClientID);
        EdgeNode thisNode = getNodeByID(thisNodeID);

        //Get client used resource
        long usedResource = thisClient.getReqResource();
        long usedNetwork = thisClient.getReqNetwork();

        //Get node location in list to replace later on
        int nodeLocationInList = this.nodeList.indexOf(thisNode);
        //node that we going to replace in our nodeList
        EdgeNode updateNode = new EdgeNode(thisNodeID, thisNode.getIpAddress(), thisNode.isConnected(), thisNode.getTotalResource(), thisNode.getTotalNetwork(), thisNode.getLocation());

        //accounting usedResource (resource - usedResource)
        updateNode.updateComputingResource(usedResource);
        updateNode.updateNetworkBandwidth(usedNetwork);

        logger.info("Node [{}] before assigned to client [{}]: \n{}", thisNodeID, thisClientID, this.nodeList.get(nodeLocationInList));
        this.nodeList.set(nodeLocationInList, updateNode);
        logger.info("Node [{}] after assigned to client [{}]: \n{}", thisNodeID, thisClientID, this.nodeList.get(nodeLocationInList));
    }


    /**
     * Update node's resources after disconnect with client (resource got freed up)
     * We plus used_resource by client to node.
     *
     * @param clientAsString JSON body of client when updating
     * @author Zero
     */
    private void updateAfterDisconnecting(String clientAsString) {
        try {
            //get client ID and disconnect reason:
            //we only need the ID and message of this object anyway
            EdgeClient pseudoClient = this.mapper.readValue(clientAsString, EdgeClient.class);
            String message = pseudoClient.getMessage();
            String thisClientID = pseudoClient.getId();
            long disconnectedTime = System.currentTimeMillis();

            //this is the one that registered in the list
            EdgeClient thisClient = getClientByID(thisClientID);
            logger.info("disconnected client [{}] : \n{}", thisClientID, thisClient);

            //Initiate variables
            logger.info("mapping: {}", mapping);

            //TODO: Throw an exception when can't find client in the mapping
            if (!mapping.containsKey(thisClientID)) {
                logger.error("This client [{}] is not connected to any nodes in the system", thisClientID);
                throw new Exception("This client [" + thisClientID + "] is not connected to any nodes in the system");
            }
            //Get node that assigned to client
            String thisNodeID = this.mapping.get(thisClientID);
            EdgeNode thisNode = getNodeByID(thisNodeID);

            //remove the coupling in mapping object
            this.mapping.remove(thisClientID, thisNodeID);

            logger.info("Node [{}] before disconnect from client [{}]: \n{}", thisNodeID, thisClientID, thisNode);

            /*
            -----------------------------UPDATING NODE INFO----------------------------------------------------------
             */
            //If node is still assigned to other client
            boolean nodeIsAssigned = mapping.containsValue(thisNodeID);
            long usedResource = 0;
            long usedNetwork = 0;

            //Then calculate the accumulated resource & network by other clients
            if (nodeIsAssigned){
                ArrayList<String> assignedClients = getAssignedClientsInList(thisNodeID);
                for ( String client : assignedClients){
                    thisClient = getClientByID(client);
                    usedNetwork =  usedNetwork + thisClient.getReqNetwork();
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
            thisClientHistoryInfo.setLastConnectedTime(thisNodeID,disconnectedTime);

            //Print out for debugging purposes:
            logger.info("History after disconnecting: \n{}", thisClientHistoryInfo);


        } catch (Exception e) {
            logger.error("error in disconnecting client");
            e.printStackTrace();
        }

    }


    /*
    ---------------------------------------------------------------------------
    DONE: Figure out exactly how this function work.
    We have spliting regex from response string here.
     */
    @Override
    public void out(String response) throws Exception {

        logger.info("[out function] \nresponse string: {} ", response);
        String[] commandLine = response.split(" ");
        logger.info("after split response: {}", Arrays.toString(commandLine));
        /*
        After split, command array gonna look like this:
        [matchMaker, update_node, {"id":"node3","ipAddress":"68.131.232.215:30968","connected":true,"resource":50,"network":90}]
        Parse them to the if and switch bunch below to execute different functions
         */
        if (commandLine[0].equals("matchMaker")) {
            try {
                switch (commandLine[1]) {
                    case "register_client":
                        registerClient(commandLine[2]);
                        logger.info("""
                                client registered, done with [outfunction]
                                ---------------------------------------------------------------------------------------
                                """);
                        break;

                    case "register_node":
                        registerNode(commandLine[2]);
                        logger.info("""
                                node registered, done with [outfunction]
                                ---------------------------------------------------------------------------------------
                                """);
                        break;

                    case "assign_client":
                        assign(commandLine[2]);
                        logger.info("""
                                client assigned, done with [outfunction]
                                ---------------------------------------------------------------------------------------
                                """);
                        break;

                    case "disconnect_client":
                        updateAfterDisconnecting(commandLine[2]);
                        logger.info("""
                                client disconnected, done with [outfunction]
                                ---------------------------------------------------------------------------------------
                                """);
                        break;

                    default:
                        throw new IllegalArgumentException("Invalid command for MatchMaker");
                }
            } catch (IndexOutOfBoundsException e) {
                throw new IllegalArgumentException("Arguments missing for command - MatchMaker");
            }
        }
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
    private void registerClient(String clientAsString) {
        try {
            logger.info("RegisterClient - client as string : {} ", clientAsString);
            //Map the contents of the JSON file to a java object
            EdgeClient thisClient = this.mapper.readValue(clientAsString, EdgeClient.class);
            String thisClientID = thisClient.getId();

            //Check whether client is already register or not
            //If not -> add client to the list, else update client info
            if (!checkClientInList(thisClient)) {
                logger.info("client after readValue from mapper: {}", thisClient);
                this.clientList.add(thisClient);
            }

            //Update client info if this client is already in the list
            else {
                logger.info("this client has already been registered, updating {} stats", thisClientID);
                updateClient(clientAsString);
            }

            //Initiating client history
            EdgeClientHistory thisClientHistory = new EdgeClientHistory(thisClientID);

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

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
    private void registerNode(String nodeAsString) throws Exception {
        try {
            logger.info("RegisterNode - node as string: {} ", nodeAsString);
            EdgeNode thisNode = this.mapper.readValue(nodeAsString, EdgeNode.class);

            //Check whether node is already register or not
            //If not -> add node to the list, else node client info
            if (!checkNodeInList(thisNode)) {
                logger.info("node after readValue from mapper: {}", thisNode);
                this.nodeList.add(thisNode);
            }
            //Update node info if this client is already in the list
            else {
                logger.info("this node has already been registered, updating {} stats", thisNode.getId());
                updateNode(nodeAsString);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Check for duplication of node within the list
     *
     * @param thisNode
     * @return true if duplicated, false if it's unique (not in list yet)
     */
    private boolean checkNodeInList(EdgeNode thisNode) {

        //If there is a node that have the same name with thisNode in parameter, it's a duplicated
        for (EdgeNode node : this.nodeList) {
            if (node.getId().equals(thisNode.getId())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check for duplication of client within the list
     *
     * @param thisClient the client we want to check whether it is in the list or not
     * @return true if duplicated, false if it's unique (not in list yet)
     */
    private boolean checkClientInList(EdgeClient thisClient) {

        //If there is a node that have the same name with thisNode in parameter, it's a duplicated
        for (EdgeClient client : this.clientList) {
            if (client.getId().equals(thisClient.getId())) {
                return true;
            }
        }
        return false;
    }


    /**
     * Update the node with new info/data from REST input (JSON body)
     *
     * @param nodeAsString JSON string of updating node
     * @throws Exception
     */
    private void updateNode(String nodeAsString) throws Exception {
        logger.info("UpdateNode - node as string: {} ", nodeAsString);
        //Map the contents of the JSON file to a java object
        EdgeNode newNode = this.mapper.readValue(nodeAsString, EdgeNode.class);

        //newNode's data
        String newNodeID = newNode.getId();

        //Get updating node location in list
        int thisNodeLocation = nodeList.indexOf(getNodeByID(newNodeID));
        EdgeNode oldNode = nodeList.get(thisNodeLocation);

        //Check whether node is assigned, if yes then we will deduct the accumulated network and resource when updating
        boolean nodeIsAssigned = mapping.containsValue(newNodeID);
        long usedResource = 0;
        long usedNetwork = 0;
        if (nodeIsAssigned) {
            ArrayList<String> assignedClientsList = getAssignedClientsInList(newNodeID);
            for (String client : assignedClientsList) {
                EdgeClient thisClient = getClientByID(client);
                usedNetwork = usedNetwork + thisClient.getReqNetwork();
                usedResource = usedResource + thisClient.getReqResource();
            }
            logger.debug("usedNetwork : {}         usedResource : {}", usedNetwork, usedResource);
        }

        //Reassign old value if there aren't any new value (default value = Long.MAX_VALUE when unassigned)
        if (newNode.getIpAddress().equalsIgnoreCase("null")) {
            logger.debug("there are no new ip address to update, reuse the old one");
            newNode.setIpAddress(oldNode.getIpAddress());
        }
        if (newNode.getTotalResource() == Long.MAX_VALUE) {
            logger.debug("there are no new total computing resource to update, reuse the old one");
            newNode.setTotalResource(oldNode.getTotalResource());
        }
        if (newNode.getTotalNetwork() == Long.MAX_VALUE) {
            logger.debug("there are no new total network bandwidth to update, reuse the old one");
            newNode.setTotalNetwork(oldNode.getTotalNetwork());
        }
        if (newNode.getLocation() == Long.MAX_VALUE) {
            logger.debug("there are no new location to update, reuse the old one");
            newNode.setLocation(oldNode.getLocation());
        }

        //minus the occupied resource from assigned clients
        newNode.updateNetworkBandwidth(usedNetwork);
        newNode.updateComputingResource(usedResource);

        logger.info("[{}] before update\n{}", newNodeID, nodeList.get(thisNodeLocation));
        //replace the oldNode with this newNode
        nodeList.set(thisNodeLocation, newNode);
        logger.info("[{}] after update\n{}", newNodeID, nodeList.get(thisNodeLocation));

    }

    private void updateClient(String clientAsString) throws Exception {
        //Map the contents of the JSON file to a java object
        EdgeClient newClient = this.mapper.readValue(clientAsString, EdgeClient.class);
        //get updating client location
        EdgeClient oldClient = getClientByID(newClient.getId());
        int thisClientLocation = clientList.indexOf(oldClient);

        //Reassign old value if there aren't any new value (default value = Long.MAX_VALUE when unassigned)
        if (newClient.getReqResource() == Long.MAX_VALUE) {
            logger.debug("there are no new required computing resource to update, reuse the old one");
            newClient.setReqResource(oldClient.getReqResource());
        }
        if (newClient.getReqNetwork() == Long.MAX_VALUE) {
            logger.debug("there are no new required network bandwidth address to update, reuse the old one");
            newClient.setReqNetwork(oldClient.getReqNetwork());
        }
        if (newClient.getLocation() == Long.MAX_VALUE) {
            logger.debug("there are no new location to update, reuse the old one");
            newClient.setLocation(oldClient.getLocation());
        }
        //remap the old client history to new client history
        newClient.setClientHistory(oldClient.getClientHistory());

        //replace new old client with the new client
        clientList.set(thisClientLocation, newClient);
    }


    /*/
    This happen after assign (or match making), sending command such as [give_node client1 node1] to the server (i guess)
     */
    @Override
    public String read() throws Exception {
        if (command.isEmpty()) {
            throw new Exception("No command exception");
        }
        //fetch the command that was changed by function [assign] above,
        String toSend = command;
        //this line to reset command (class variable) to empty

        command = "";
        return toSend;
    }

    public List<EdgeNode> getNodeList() {
        return nodeList;
    }

    public List<EdgeClient> getClientList() {
        return clientList;
    }

    public Map<String, String> getMapping() {
        return mapping;
    }

    /*
    ----------------------Just making sure input client are in client list---------------
     */
    private EdgeClient getClientByID(String clientID) throws Exception {
        for (EdgeClient thisClient : this.clientList) {
            if (thisClient.getId().equals(clientID)) {
                return thisClient;
            }
        }
        throw new Exception("can not find this [clientID] in the list");
    }

    private EdgeNode getNodeByID(String nodeID) throws Exception {
        for (EdgeNode thisNode : this.nodeList) {
            if (thisNode.getId().equals(nodeID)) {
                return thisNode;
            }
        }
        throw new Exception("can not find this [nodeID] in the list");
    }

    /**
     * get a list of client that connected to nodeID
     *
     * @param nodeID
     * @return list of client assigned to nodeID
     * @author Zero
     */
    private ArrayList<String> getAssignedClientsInList(String nodeID) {
        Set<String> assignedClientList = this.mapping.keySet();
        ArrayList<String> returnClientList = new ArrayList<>();
        for (String thisClient : assignedClientList) {
            String theNode = this.mapping.get(thisClient);
            if (theNode.equals(nodeID)) {
                //Add the client that is assigned to nodeID to the return list
                returnClientList.add(thisClient);
                logger.debug("adding client {} to list\n {}", thisClient, returnClientList);
            }
        }
        //return a list of client assigned to nodeID
        return returnClientList;
    }

}
