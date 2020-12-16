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

    //History list of edge node and client
    //DONE: implement a new class for history, using hashmap like this is too annoying
    //DONE: implement timing in history (decay history score by time)
    //DONE: Figure out whether this history should be a global variable to share across all instances, or use it locally

    //Hashmap contain <NodeId, HashMap<ClientID, historyScore>>

    /*
    TODO: make logger for  debugging (use a boolean variable will be fine) to reduce the console output later on
    TODO: update client and node's data after assigned.
     */


    /*
    -----------------------start_match_m start from here--------------------------------------------------
     */
    public MatchMaker(String name, String algorithmType) {
        super(name);

        //Manually changed algorithm used here. -negative, can't use
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
            default -> throw new IllegalArgumentException("Invalid type for MatchMaker");
        }
    }

    private void setAlgorithm(MatchMakingAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    /*
    -----------------------Assigning client to node--------------------------------------------------
    Single client to single node, not multiple in the same time
    */
    private void assign(String thisClientID) {

        try {
            EdgeClient thisClient = this.getClientByID(thisClientID);
            EdgeClientHistory clientHistoryInfo = clientHistoryHashMap.get(thisClientID);

            //Check whether thisClient has already been matched or not

            if (mapping.containsKey(thisClientID)) {
                String connectedNode = mapping.get(thisClientID);
                logger.warn("this client is already connected with {}", connectedNode);
                logger.info("client history info: \n{}", clientHistoryInfo);
                logger.info("mapping map: {}", mapping);
                throw new Exception("this ["+thisClientID+"] is already connected with " + connectedNode);
            } else {
                //match client with node in nodelist according to algorithm
                EdgeNode thisNode = this.algorithm.match(thisClient, this.nodeList, clientHistoryInfo);
                String thisNodeID = thisNode.getId();
                //list of node, for debugging purposes
                for (EdgeNode theNode : nodeList) {
                    logger.debug("{}", theNode);
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


        //get node available resource
        String nodeIP = thisNode.getIpAddress();
        boolean nodeConnect = thisNode.isConnected();
        long nodeResource = thisNode.getResource();
        long nodeNetwork = thisNode.getNetwork();
        long nodeLocation = thisNode.getLocation();
        long nodeTotalResource = thisNode.getTotalResource();
        long nodeTotalNetwork = thisNode.getTotalNetwork();
        int nodeLocationInList = this.nodeList.indexOf(thisNode);

        //Calculating occupied resource, network. This is the minus part mentioned in javadoc
        nodeResource = nodeResource - usedResource;
        nodeNetwork = nodeNetwork - usedNetwork;

        //node that we going to replace in our nodeList
        EdgeNode updateNode = new EdgeNode(thisNodeID, nodeIP, nodeConnect, nodeResource, nodeTotalResource, nodeNetwork, nodeTotalNetwork, nodeLocation);

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
            String thisClientID = pseudoClient.getId();
            long disconnectedTime = System.currentTimeMillis();

            //this is the one that registered in the list
            EdgeClient thisClient = getClientByID(thisClientID);
            logger.info("disconnected client [{}] : \n{}", thisClientID, thisClient);

            //Initiate variables
            logger.info("mapping: {}", mapping);
            String thisNodeID = this.mapping.get(thisClientID);
            if (thisNodeID == null) {
                logger.error("This client [{}] is not connected to any nodes in the system", thisClientID);
                //TODO: create a boolean [DEBUG] variable to toggle between using exception to catch error or just skip
                return;
            }
            EdgeNode thisNode = getNodeByID(thisNodeID);

            //Get client used resource
            long usedResource = thisClient.getReqResource();
            long usedNetwork = thisClient.getReqNetwork();

            //Get message (job_done, job_fail, too_far etc) from pseudo client above
            String message = pseudoClient.getMessage();

            //get node available resource
            String nodeIP = thisNode.getIpAddress();
            boolean nodeConnect = thisNode.isConnected();
            long nodeResource = thisNode.getResource();
            long nodeNetwork = thisNode.getNetwork();
            long nodeLocation = thisNode.getLocation();
            long nodeTotalResource = thisNode.getTotalResource();
            long nodeTotalNetwork = thisNode.getTotalNetwork();

            int nodeLocationInList = this.nodeList.indexOf(thisNode);

            //Calculating occupied resource, network. This is the plus part mentioned in javadoc
            nodeResource = nodeResource + usedResource;
            nodeNetwork = nodeNetwork + usedNetwork;

            //node that we going to replace in our nodeList
            EdgeNode updateNode = new EdgeNode(thisNodeID, nodeIP, nodeConnect, nodeResource, nodeTotalResource, nodeNetwork, nodeTotalNetwork, nodeLocation);

            logger.info("Node [{}] before disconnect from client [{}]: \n{}", thisNodeID, thisClientID, this.nodeList.get(nodeLocationInList));

            //update node's info
            this.nodeList.set(nodeLocationInList, updateNode);
            logger.info("Node [{}] after disconnect from client [{}]: \n{}", thisNodeID, thisClientID, this.nodeList.get(nodeLocationInList));

            //remove the coupling in mapping object
            this.mapping.remove(thisClientID, thisNodeID);

            //---------------------------------------------HISTORY TIME-------------------------------------------------
            EdgeClientHistory thisClientHistoryInfo = clientHistoryHashMap.get(thisClientID);

            //Get score between thisClientID and thisNodeID
            Long historyScore = thisClientHistoryInfo.getHistoryScore(thisClientID, thisNodeID);

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
            thisClientHistoryInfo.setHistoryScoreForClient(thisClientID, thisNodeID, historyScore);

            //Add client's disconnected time to history info package
            thisClientHistoryInfo.setConnectedTimeForClient(thisClientID, thisNodeID, disconnectedTime);

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
    public void out(String response) throws IllegalArgumentException {

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

                    case "update_node":
                        updateNode(commandLine[2]);
                        logger.info("""
                                node updated, done with [outfunction]
                                ---------------------------------------------------------------------------------------
                                """);
                        break;

                    case "update_client":
                        updateClient(commandLine[2]);
                        logger.info("""
                                client updated, done with [outfunction]
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

    /*
    -----------------------register client to a list--------------------------------------------------
     */
    private void registerClient(String clientAsString) {
        try {
            logger.info("RegisterClient - client as string : {} ", clientAsString);
            //Map the contents of the JSON file to a java object
            EdgeClient thisClient = this.mapper.readValue(clientAsString, EdgeClient.class);
            String thisClientID = thisClient.getId();

            //If client is not registered (no duplication) -> moving on
            if (!checkDuplicateClientInList(thisClient)) {
                //Just for debugging purpose
                logger.info("client after readValue from mapper: {}", thisClient);
                this.clientList.add(thisClient);
            } else {
                logger.warn("this client has already been registered (duplicated ID exist)");
            }

            //Initiating client history
            EdgeClientHistory thisClientHistory = new EdgeClientHistory(thisClientID);
            for (EdgeNode thisNode : this.nodeList) {
                String thisNodeID = thisNode.getId();
                thisClientHistory.setHistoryScoreForClient(thisClientID, thisNodeID, 0L);
                thisClientHistory.setConnectedTimeForClient(thisClientID, thisNodeID, 0L);
//                thisClientHistory.setConnectedMap(thisClientID, thisNodeID, 0L);
            }

            //Put the client with its history in a HashMap, we can always update the history later on
            // by fetching thisClientHistory stuff anyway
            logger.info("this client history info: \n{}", thisClientHistory);
            clientHistoryHashMap.put(thisClientID, thisClientHistory);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }


    /*
    -----------------------register node to a list--------------------------------------------------
     */
    private void registerNode(String nodeAsString) {
        try {
            logger.info("RegisterNode - node as string: {} ", nodeAsString);
            EdgeNode thisNode = this.mapper.readValue(nodeAsString, EdgeNode.class);
            if (!checkDuplicateNodeInList(thisNode)) {
                logger.info("node after readValue from mapper: {}", thisNode);
                this.nodeList.add(thisNode);
            } else {
                logger.warn("this node has already been registered (duplicated ID exist)");
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
    private boolean checkDuplicateNodeInList(EdgeNode thisNode) {

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
     * @param thisClient
     * @return true if duplicated, false if it's unique (not in list yet)
     */
    private boolean checkDuplicateClientInList(EdgeClient thisClient) {

        //If there is a node that have the same name with thisNode in parameter, it's a duplicated
        for (EdgeClient client : this.clientList) {
            if (client.getId().equals(thisClient.getId())) {
                return true;
            }
        }
        return false;
    }

    /*
   -----------------------update node to the list--------------------------------------------------
    */
    private void updateNode(String nodeAsString) {
        try {
            logger.info("UpdateNode - node as string: {} ", nodeAsString);
            //Map the contents of the JSON file to a java object
            EdgeNode newNode = this.mapper.readValue(nodeAsString, EdgeNode.class);
            //newNode's data
            String newNodeID = newNode.getId();
            long newNodeResource = newNode.getResource();
            long newNodeNetwork = newNode.getNetwork();
            long newNodeTotalNetwork = newNode.getTotalNetwork();
            long newNodeTotalResource = newNode.getTotalResource();
            String newNodeIP = newNode.getIpAddress();
            boolean newNodeConnect = newNode.isConnected();
            long newNodeLocation = newNode.getLocation();

            //get updating node location
            Integer thisNodeLocation = null;
            EdgeNode oldNode = getNodeByID(newNodeID);
            long oldNodeResource = oldNode.getResource();
            long oldNodeNetwork = oldNode.getNetwork();
            thisNodeLocation = this.nodeList.indexOf(oldNode);

            logger.info("[{}] before update\n{}", newNodeID, oldNode);

            //check whether this node is connected to a client or not
            boolean nodeIsAssigned = this.mapping.containsValue(newNodeID);

            //calculate used resources to update later on if that node is assigned to others
            if (nodeIsAssigned) {
                ArrayList<String> assignedClientList = getAssignedClientsInList(newNodeID);
                long consumedNetwork = 0;
                long consumedResource = 0;
                //Get total consumed resource and network
                for (String clientID : assignedClientList) {
                    EdgeClient thisClient = getClientByID(clientID);
                    long usedResource = thisClient.getReqResource();
                    long usedNetwork = thisClient.getReqNetwork();
                    consumedNetwork = consumedResource + usedNetwork;
                    consumedResource = consumedResource + usedResource;
                }

                //only update resource and network if we do have those variable in JSON body.
                if ((newNodeNetwork < Long.MAX_VALUE)) {
                    //there is info about new node available network
                    newNodeNetwork = newNodeNetwork - consumedNetwork;
                } else {
                    logger.info("no info about new available resource");
                    newNodeNetwork = oldNodeNetwork;
                }

                if ((newNodeResource < Long.MAX_VALUE)) {
                    //there is info about new node available resource
                    newNodeResource = newNodeResource - consumedResource;
                } else {
                    logger.info("no info about new available network");
                    newNodeResource = oldNodeResource;
                }
                logger.info("this node assigned clients : {}\nConsumed resource and network : {} - {} ",
                        assignedClientList, consumedResource, consumedNetwork);
                logger.info("this [{}] info: \n", newNodeID);
            }  //when there is no clients assigned to the node
            else {
                logger.info("this [{}] is not connected to any client", newNodeID);
                if ((newNodeNetwork < Long.MAX_VALUE)) {
                    //there is info about new node available network
                } else {
                    logger.info("no info about new available resource");
                    newNodeNetwork = oldNodeNetwork;
                }

                if ((newNodeResource < Long.MAX_VALUE)) {
                    //there is info about new node available resource
                } else {
                    logger.info("no info about new available network");
                    newNodeResource = oldNodeResource;
                }
            }

            //Reassign old value if there aren't any new value (default value = Long.MAX_VALUE when unassigned
            if (newNodeIP.equalsIgnoreCase("null")) {
                newNodeIP = oldNode.getIpAddress();
            }
            if (newNodeTotalResource >= Long.MAX_VALUE) {
                newNodeTotalResource = oldNode.getTotalResource();
            }
            if (newNodeTotalNetwork >= Long.MAX_VALUE) {
                newNodeTotalNetwork = oldNode.getTotalNetwork();
            }
            if (newNodeLocation >= Long.MAX_VALUE) {
                newNodeLocation = oldNode.getLocation();
            }


            /*
            By the time we get to this step, we should have these parameter prepared
            nodeID - nodeIP - nodeConnect - nodeResource - nodeTotalResource - nodeNetwork - nodeTotalNetwork - nodeLocation
             */
            EdgeNode updateNode = new EdgeNode(newNodeID, newNodeIP, newNodeConnect, newNodeResource, newNodeTotalResource, newNodeNetwork, newNodeTotalNetwork, newNodeLocation);
            this.nodeList.set(thisNodeLocation, updateNode);
            logger.info("[{}] after update\n{}", newNodeID, updateNode);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
-----------------------update client to the list--------------------------------------------------
*/
    private void updateClient(String clientAsString) {
        try {
            logger.info("UpdateClient - client as string: {} ", clientAsString);
            //Map the contents of the JSON file to a java object
            EdgeClient newClient = this.mapper.readValue(clientAsString, EdgeClient.class);
            //get updating client location
            Integer thisClientLocation = null;
            String oldClientID = newClient.getId();

            for (EdgeClient client : this.clientList) {
                logger.info("checking node: {}", client.getId());
                if (client.getId().equalsIgnoreCase(oldClientID)) {
                    logger.info("oldClient: {}\n newClient: {}", client, newClient);
                    logger.info("clientID matched with new node [{}], leaving", newClient.getId());
                    thisClientLocation = this.clientList.indexOf(client);
                    //get out when found, no need for further exploration
                    break;
                }
            }
            //updating process
            if (thisClientLocation == null) {
                logger.error("new client ID does not match with any client's ID in the system.");
            }
            this.clientList.set(thisClientLocation, newClient);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
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
        logger.info("THIS IS THE COMMANNDDDDDD IN READDDDDD {}", command);
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
