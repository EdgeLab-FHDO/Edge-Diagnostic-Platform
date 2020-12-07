package InfrastructureManager.MatchMaking;

import InfrastructureManager.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
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
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private MatchMakingAlgorithm algorithm;
    private final ObjectMapper mapper;
    private String command = "";

    private final List<EdgeNode> nodeList;
    private final List<EdgeClient> clientList;
    //map - clientID, nodeID
    private final Map<String, String> mapping;
    private static MatchMaker instance = null;

    //History list of edge node and client
    //TODO: Figure out whether this history should be a global variable to share across all instances, or use it locally

    //Hashmap contain <NodeId, HashMap<ClientID, historyScore>>
//    private final HashMap<String, HashMap<String, Long>> nodeHistory = new HashMap<>();
    Multimap<String, HashMap<String, Long>> nodeHistory = ArrayListMultimap.create();

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
//        algorithmType = "sbmm";
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
            case "random":
//                setAlgorithm(new ScoreBasedMM());
                setAlgorithm(new RandomMatchMaking());
                break;

            case "sbmm":
                setAlgorithm(new ScoreBasedMM());
                break;

            default:
                throw new IllegalArgumentException("Invalid type for MatchMaker");
        }
    }

    private void setAlgorithm(MatchMakingAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    /*
    -----------------------Assigning client to node--------------------------------------------------
    Single client to single node, not multiple in the same time
    */
    private void assign(String clientID) {

        logger.info("assigning client to node \n ----------------------------------------------------------------------------------------------------");
        try {
            EdgeClient client = this.getClientByID(clientID);
            //match client with node in nodelist according to algorithm
            EdgeNode node = this.algorithm.match(client, this.nodeList, this.nodeHistory);

            //list of node, for debugging purposes
            for (EdgeNode theNode : nodeList) {
                logger.info(theNode.toString());
            }

            logger.info("\n\n assigned node info: {}", node.toString());
            logger.info("client info: {}", client.toString());

            if (node == null) {
                logger.warn("no node in nodelist");
            } else {
                logger.info("assign client [{}] to node [{}] ", client.getId(), node.getId());
                //DONE: update node's data
                if (!node.getId().equals("rejectNode")) {
                    updateAfterAssigning(client.getId(), node.getId());
                    command = "give_node " + client.getId() + " " + node.getId();
                } else {
                    //no out command if we couldn't assign the client to any node. No need for further operation here
                    //TODO: maybe send a rejected response if this happen
                    command = "";
                }
                //DONE: mapping should take only client ID, because of object referencing
                this.mapping.put(client.getId(), node.getId());
                logger.info("mapping map: {} \n done with assigning ------------------------------------------------------------------------------------------", mapping);
            }
        } catch (Exception e) {
            logger.error("error in assigning client");
            e.printStackTrace();
        }
    }

    //TODO: update node data after disconnecting, remove it from mapper

    /**
     * Update node's data after assigning.
     * We minus resourced occupied by client
     *
     * @param thisClientID
     * @param thisNodeID
     * @author Zero
     */
    private void updateAfterAssigning(String thisClientID, String thisNodeID) throws Exception {

        EdgeClient thisClient = getClientByID(thisClientID);
        EdgeNode thisNode = getNodeByID(thisNodeID);

        //Get client used resource
        long usedResource = thisClient.getReqResource();
        long usedNetwork = thisClient.getReqNetwork();


        //get node available resource
        //TODO: maybe use setter/getter rather than replace the whole object when update?
        String node_IP = thisNode.getIpAddress();
        boolean node_connect = thisNode.isConnected();
        long node_Resource = thisNode.getResource();
        long node_Network = thisNode.getNetwork();
        long node_Location = thisNode.getLocation();
        long node_Total_Resource = thisNode.getTotalResource();
        long node_Total_Network = thisNode.getTotalNetwork();
        int node_Location_in_list = this.nodeList.indexOf(thisNode);

        //Calculating occupied resource, network. This is the minus part mentioned in javadoc
        node_Resource = node_Resource - usedResource;
        node_Network = node_Network - usedNetwork;

        //node that we going to replace in our nodeList
        EdgeNode updateNode = new EdgeNode(thisNodeID, node_IP, node_connect, node_Resource, node_Total_Resource, node_Network, node_Total_Network, node_Location);

        logger.info("Node [{}] before assigned to client [{}]: \n{}", thisNodeID, thisClientID, this.nodeList.get(node_Location_in_list).toString());
        this.nodeList.set(node_Location_in_list, updateNode);
        logger.info("Node [{}] after assigned to client [{}]: \n{}", thisNodeID, thisClientID, this.nodeList.get(node_Location_in_list).toString());

    }


//TODO: everytime we register a node, we also register that node to our hashmap as key.

    /**
     * Update node's resources after disconnect with client (resource got freed up)
     * We plus used_resource by client to node.
     *
     * @param clientAsString
     * @author Zero
     */
    private void updateAfterDisconnecting(String clientAsString) {
        try {
            //get client ID and disconnect reason:
            //we only need the ID and message of this object anyway
            EdgeClient pseudoClient = this.mapper.readValue(clientAsString, EdgeClient.class);
            String thisClientID = pseudoClient.getId();

            //this is the one that registered in the list
            EdgeClient thisClient = getClientByID(thisClientID);
            logger.info("disconnected client [{}] : \n{}", thisClientID, thisClient.toString());

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
            //TODO: maybe use setter/getter rather than replace the whole object when update?
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

            logger.info("Node [{}] before disconnect from client [{}]: \n{}", thisNodeID, thisClientID, this.nodeList.get(nodeLocationInList).toString());

            //update node's info
            this.nodeList.set(nodeLocationInList, updateNode);
            logger.info("Node [{}] after disconnect from client [{}]: \n{}", thisNodeID, thisClientID, this.nodeList.get(nodeLocationInList).toString());

            //remove the coupling in mapping object
            this.mapping.remove(thisClientID, thisNodeID);

            //Get node history (all clients connected)
            Collection<HashMap<String, Long>> thisNodeHistory = nodeHistory.get(thisNodeID);

            //Get the history with thisClientID
            Long thisNodeHistoryWithClient;
            for (HashMap<String, Long> history : thisNodeHistory) {
                if (history.containsKey(thisClientID)) {
                    thisNodeHistoryWithClient = history.get(thisClientID);
                    logger.info("\nAccessing path: getNode -> Client -> before_score \n {}    ->    {}     ->    {}", thisNodeID, thisClientID, thisNodeHistoryWithClient);
                    //Calculate the score depends on reason disconnected
                    switch (message) {
                        case "job_done" -> {
                            thisNodeHistoryWithClient = thisNodeHistoryWithClient - 5;
                        }
                        case "job_failed" -> {
                            thisNodeHistoryWithClient = thisNodeHistoryWithClient + 10;
                        }
                    }
                    if (thisNodeHistoryWithClient < 0) {
                        thisNodeHistoryWithClient = 0L;
                    }
                    history.put(thisClientID, thisNodeHistoryWithClient);
                    break;
                }
            }

            //Print out for debugging purposes:
            Set<String> thisNodeList = nodeHistory.keySet();
            logger.info("History after disconnecting:");
            for (String thisNodeInList : thisNodeList) {
                logger.info("{} -> {}", thisNodeInList, nodeHistory.get(thisNodeInList));
            }


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
        String[] command = response.split(" ");
        logger.info("after split response: {}", Arrays.toString(command));
        /*
        After split, command array gonna look like this:
        [matchMaker, update_node, {"id":"node3","ipAddress":"68.131.232.215:30968","connected":true,"resource":50,"network":90}]
        Parse them to the if and switch bunch below to execute different functions
         */
        if (command[0].equals("matchMaker")) {
            try {
                switch (command[1]) {
                    case "register_client":
                        registerClient(command[2]);
                        logger.info("client registered, done with [outfunction]\n\n");
                        break;

                    case "register_node":
                        registerNode(command[2]);
                        logger.info("node registered, done with [outfunction]\n\n");
                        break;

                    case "assign_client":
                        assign(command[2]);
                        logger.info("client assigned, done with [outfunction]\n\n");
                        break;

                    case "update_node":
                        updateNode(command[2]);
                        logger.info("node updated, done with [outfunction]\n\n");
                        break;

                    case "update_client":
                        updateClient(command[2]);
                        logger.info("client updated, done with [outfunction]\n\n");
                        break;

                    case "disconnect_client":
                        updateAfterDisconnecting(command[2]);
                        logger.info("client disconnected, done with [outfunction]\n\n");
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
                String clientPretty = this.mapper.writerWithDefaultPrettyPrinter().writeValueAsString(thisClient);
                logger.info("client after readValue from mapper: {}", clientPretty);
                this.clientList.add(thisClient);
            } else {
                logger.warn("this client has already been registered (duplicated ID exist)");
            }


            for (EdgeNode thisNode : this.nodeList) {
                String thisNodeID = thisNode.getId();
                //Add client into the node history with default history value = 0;
                HashMap<String, Long> clientMap = new HashMap<>();
                /*
                Have to do this step in this loop.
                Do it outside will result in the same object got put in 3 different places, making
                changing 1 of them result into change all places
                 */
                clientMap.put(thisClientID, 0L);
                nodeHistory.put(thisNodeID, clientMap);
            }
            logger.info("nodeHistory: {} ", nodeHistory);

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
            String thisNodeID = thisNode.getId();

            if (!checkDuplicateNodeInList(thisNode)) {
                String nodePretty = this.mapper.writerWithDefaultPrettyPrinter().writeValueAsString(thisNode);
                logger.info("node after readValue from mapper: {}", nodePretty);
                this.nodeList.add(thisNode);
            } else {
                logger.warn("this node has already been registered (duplicated ID exist)");
            }

            //Add new node into our hashmap
            logger.info("nodeHistory: {} ", nodeHistory);


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

            //get updating node location
            Integer thisNodeLocation = null;
            String oldNodeID = newNodeID;
            EdgeNode oldNode = getNodeByID(oldNodeID);
            long oldNodeResource = oldNode.getResource();
            long oldNodeNetwork = oldNode.getNetwork();
            thisNodeLocation = this.nodeList.indexOf(oldNode);
            //updating process
            if (thisNodeLocation == null) {
                //TODO: this is rather redundance because getNodeByID would already did this job
                logger.error(">>>>>>>>>>>>>>>>>>>>>>>>>>>> UPDATE FAILED <<<<<<<<<<<<<<<<<<<<<<<<< \nnew node ID does not match with any node's ID in the system.");
                return;
            }

            //check whether this node is connected to a client or not
            boolean nodeIsAssigned = this.mapping.containsValue(oldNodeID);

            //calculate used resources to update later on if that node is assigned to others
            if (nodeIsAssigned) {
                ArrayList<String> assignedClientList = getAssignedClient(oldNodeID);
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
                if (!(newNodeNetwork >= Long.MAX_VALUE)) {
                    //there is info about new node available network
                    newNodeNetwork = newNodeNetwork - consumedNetwork;
                } else {
                    logger.info("no info about new available resource");
                    newNodeNetwork = oldNodeNetwork;
                }

                if (!(newNodeResource >= Long.MAX_VALUE)) {
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
                logger.info("this [{}] is not connected to any client");
                if (!(newNodeNetwork >= Long.MAX_VALUE)) {
                    //there is info about new node available network
                } else {
                    logger.info("no info about new available resource");
                    newNodeNetwork = oldNodeNetwork;
                }

                if (!(newNodeResource >= Long.MAX_VALUE)) {
                    //there is info about new node available resource
                } else {
                    logger.info("no info about new available network");
                    newNodeResource = oldNodeResource;

                }
            }

            for (EdgeNode oldNode : this.nodeList) {
                logger.info("checking node: {}", oldNode.getId());
                if (oldNode.getId().equalsIgnoreCase(oldNodeID)) {
                    String oldNodePretty = this.mapper.writerWithDefaultPrettyPrinter().writeValueAsString(oldNode);
                    logger.info("oldNode = {}\n newNode: {}", oldNodePretty, nodePretty);
                    logger.info("nodeID matched with new node [{}], leaving", newNode.getId());
                    //get out when found, no need for further exploration
                    break;
                }
            }



            this.nodeList.set(thisNodeLocation, newNode);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
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
            String clientPretty = this.mapper.writerWithDefaultPrettyPrinter().writeValueAsString(newClient);
//            logger.info("client after readValue from mapper: {}", clientPretty);

            //get updating client location
            Integer thisClientLocation = null;
            String oldClientID = newClient.getId();

            for (EdgeClient client : this.clientList) {
                logger.info("checking node: {}", client.getId());
                if (client.getId().equalsIgnoreCase(oldClientID)) {
                    String oldClientPretty = this.mapper.writerWithDefaultPrettyPrinter().writeValueAsString(client);
                    logger.info("oldClient: {}\n newNode: {}", oldClientPretty, clientPretty);
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
        //TODO: using global variable [command] may fck things up when doing multi-thread.
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
        /*
         TODO: Can be improve/ultilize if use straight EdgeClient rather than clientID, maybe i'm wrong cuz we are using string regex to get info from JSON file.
         */
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
    private ArrayList<String> getAssignedClient(String nodeID) {
        Set<String> assignedClientList = this.mapping.keySet();
        ArrayList<String> returnClientList = new ArrayList<>();
        for (String thisClient : assignedClientList) {
            String theNode = this.mapping.get(thisClient);
            if (theNode.equals(nodeID)) {
                //Add the client that is assigned to nodeID to the return list
                returnClientList.add(thisClient);
                //TODO: change this to debug
                logger.debug("adding client {} to list\n {}", thisClient, returnClientList);
            }
        }
        //return a list of client assigned to nodeID
        return returnClientList;
    }

}
