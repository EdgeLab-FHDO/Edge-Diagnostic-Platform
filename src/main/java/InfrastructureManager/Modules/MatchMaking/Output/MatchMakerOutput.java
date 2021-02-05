package InfrastructureManager.Modules.MatchMaking.Output;

import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;
import InfrastructureManager.ModuleManagement.PlatformOutput;
import InfrastructureManager.Modules.MatchMaking.Client.EdgeClient;
import InfrastructureManager.Modules.MatchMaking.Client.EdgeClientHistory;
import InfrastructureManager.Modules.MatchMaking.Exception.*;
import InfrastructureManager.Modules.MatchMaking.MatchMakingAlgorithm;
import InfrastructureManager.Modules.MatchMaking.MatchesList;
import InfrastructureManager.Modules.MatchMaking.Node.EdgeNode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class MatchMakerOutput extends PlatformOutput {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ObjectMapper mapper;
    private final MatchesList sharedMatchesList;
    private final MatchMakingAlgorithm algorithm;
    private final List<EdgeNode> nodeList;
    private final List<EdgeClient> clientList;

    public MatchMakerOutput(ImmutablePlatformModule module, String name, MatchMakingAlgorithm algorithm, MatchesList mapping) {
        super(module,name);
        this.sharedMatchesList = mapping;
        this.algorithm = algorithm;
        this.mapper = new ObjectMapper();
        this.nodeList = new ArrayList<>();
        this.clientList = new ArrayList<>();
    }

    private void assign(String thisClientID) throws MatchMakingModuleException, JsonProcessingException {
        EdgeClient thisClient = this.getClientByID(thisClientID);
        EdgeClientHistory clientHistoryInfo = thisClient.getClientHistory();

        //If thisClient has already been matched,
        if (sharedMatchesList.getMapping().containsKey(thisClientID)) {
            String connectedNode = sharedMatchesList.getMapping().get(thisClientID);
            logger.warn("this client is already connected with {}", connectedNode);
            logger.info("client history info: \n{}", clientHistoryInfo);
            logger.info("mapping map: {}", sharedMatchesList.getMapping());
            throw new ClientAlreadyAssignedException("this [" + thisClientID + "] is already connected with " + "[" + connectedNode + "]");
        }
        //If thisClient has not been matched with any node, then we match it
        else {

            //match client with node in nodeList according to algorithm
            EdgeNode thisNode = this.algorithm.match(thisClient, nodeList);
            String thisNodeAsString = mapper.writeValueAsString(thisNode);
            String thisNodeID = thisNode.getId();

            //list of node, for debugging purposes
            nodeList.forEach(n -> logger.debug("{}",n));

            logger.info("assigned node info: \n{}", thisNode);
            logger.info("client info: \n{}", thisClient);
            logger.info("-------------- assign client [{}] to node [{}] --------------", thisClientID, thisNodeID);

            //update node's data
            updateAfterAssigning(thisClientID, thisNodeID);

            //mapping should take only client ID, because of object referencing
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
        EdgeNode updateNode = new EdgeNode(this.getOwnerModule(),thisNodeID, thisNode.getIpAddress(), thisNode.isConnected(), thisNode.getTotalResource(), thisNode.getTotalNetwork(), thisNode.getLocation());

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
                String mappedNode = sharedMatchesList.getMapping().get(thisClientID);
                throw new ClientAlreadyAssignedException("can't update because [" + thisClientID + "] already been assigned to [" + mappedNode + "]");
            }
            logger.info("this client has already been registered, updating {} stats", thisClientID);
            updateClient(clientAsString);
        }

        //Initiating client history
        EdgeClientHistory thisClientHistory = new EdgeClientHistory(this.getOwnerModule(),thisClientID);

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

    private void updateClient(String clientAsString) throws NoClientFoundException, JsonProcessingException {
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
     * @param nodeAsString  JSON string of updating node
     * @throws NoNodeFoundException If the required node cannot be found in the node list
     * @throws NoClientFoundException If the node is connected but the client cannot be found in the client list
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

        //Check whether node is assigned, if yes then we will deduct the accumulated network and resource when updating
        boolean nodeIsAssigned = sharedMatchesList.nodeIsAssigned(nodeAsString);
        long usedResource = 0;
        long usedNetwork = 0;
        if (nodeIsAssigned) {
            for (String client : sharedMatchesList.getConnectedClientsToNode(nodeAsString)) {
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
        if (newNode.getTotalResource() == 0) {
            logger.debug("there are no new total computing resource to update, reuse the old one");
            newNode.setTotalResource(oldNode.getTotalResource());
        }
        if (newNode.getTotalNetwork() == 0) {
            logger.debug("there are no new total network bandwidth to update, reuse the old one");
            newNode.setTotalNetwork(oldNode.getTotalNetwork());
        }
        if (newNode.getLocation() == 0) {
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
        EdgeNode auxNode = this.mapper.readValue(sharedMatchesList.removeClient(thisClientID),EdgeNode.class);
        String thisNodeID = auxNode.getId();
        EdgeNode thisNode = getNodeByID(thisNodeID);

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
                        registerClient(commandLine[2]);
                        logger.info("client registered, done with [outfunction]\n---------------------------------------------------------------------------------------\n");
                    }
                    case "register_node" -> {
                        registerNode(commandLine[2]);
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
