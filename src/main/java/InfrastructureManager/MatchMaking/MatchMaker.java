package InfrastructureManager.MatchMaking;

import InfrastructureManager.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


public class MatchMaker extends MasterOutput implements MasterInput {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private MatchMakingAlgorithm algorithm;
    private final ObjectMapper mapper;
    private String command = "";

    private final List<EdgeNode> nodeList;
    private final List<EdgeClient> clientList;
    private final Map<EdgeClient, EdgeNode> mapping;

    //TODO: make logger depend on debugging (use a boolean variable will be fine) to reduce the console output later on
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
                setAlgorithm(new ScoreBasedMM());
//                setAlgorithm(new RandomMatchMaking());
                break;

            case "sbmm":
                setAlgorithm(new ScoreBasedMM());

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

        logger.info("\n assigning client to node \n -------------------------");
        try {
            EdgeClient client = this.getClientByID(clientID);
            //match client with node in nodelist according to algorithm
            EdgeNode node = this.algorithm.match(client, this.nodeList);
            //list of node, for debugging purposes
            for (EdgeNode theNode : nodeList) {
                logger.info(theNode.toString());
            }

            logger.info("assigned node info: {}", node.toString());
            logger.info("client info: {}", client.toString());

            if (node == null) {
                logger.warn("no node in nodelist");
            } else {
                logger.info("assign client [{}] to node [{}] ", client.getId(), node.getId());
                this.mapping.put(client, node);
                command = "give_node " + client.getId() + " " + node.getId();
                logger.info("done with assigning -----------------------");
            }
        } catch (Exception e) {
            logger.error("error in assigning client");
            e.printStackTrace();
        }
    }


    /*
    ---------------------------------------------------------------------------
    TODO: Figure out exactly how this function work.
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
                        logger.info("client registered, done with [outfunction]\n");
                        break;

                    case "register_node":
                        registerNode(command[2]);
                        logger.info("node registered, done with [outfunction]\n");
                        break;

                    case "assign_client":
                        assign(command[2]);
                        logger.info("client assigned, done with [outfunction]\n");
                        break;

                    case "update_node":
                        updateNode(command[2]);
                        logger.info("node updated, done with [outfunction]\n");
                        break;

                    case "update_client":
                        updateClient(command[2]);
                        logger.info("client updated, done with [outfunction]\n");
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
            EdgeClient client = this.mapper.readValue(clientAsString, EdgeClient.class);

            //If client is not registered (no duplication) -> moving on
            if (!checkDuplicateClientInList(client)) {
                //Just for debugging purpose
                String clientPretty = this.mapper.writerWithDefaultPrettyPrinter().writeValueAsString(client);
                logger.info("client after readValue from mapper: {}", clientPretty);
                this.clientList.add(client);
                mapper.enable(SerializationFeature.INDENT_OUTPUT);
            } else {
                logger.warn("this client has already been registered (duplicated ID exist)");
            }
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
            EdgeNode node = this.mapper.readValue(nodeAsString, EdgeNode.class);

            if (!checkDuplicateNodeInList(node)) {
                String nodePretty = this.mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
                logger.info("node after readValue from mapper: {}", nodePretty);
                this.nodeList.add(node);
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
            String nodePretty = this.mapper.writerWithDefaultPrettyPrinter().writeValueAsString(newNode);

            //get updating node location
            Integer thisNodeLocation = null;
            String oldNodeID = newNode.getId();
            for (EdgeNode oldNode : this.nodeList) {
                logger.info("checking node: {}", oldNode.getId());
                if (oldNode.getId().equalsIgnoreCase(oldNodeID)) {
                    String oldNodePretty = this.mapper.writerWithDefaultPrettyPrinter().writeValueAsString(oldNode);
                    logger.info("oldNode = {}\n newNode: {}", oldNodePretty, nodePretty);
                    logger.info("nodeID matched with new node [{}], leaving", newNode.getId());
                    thisNodeLocation = this.nodeList.indexOf(oldNode);
                    //get out when found, no need for further exploration
                    break;
                }
            }
            //updating process
            if (thisNodeLocation == null) {
                logger.error("new node ID does not match with any node's ID in the system.");
            }

            this.nodeList.set(thisNodeLocation, newNode);
        } catch (JsonProcessingException e) {
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
        //TODO: using class variable [command] may fck things up when doing multi-thread.
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

    public Map<EdgeClient, EdgeNode> getMapping() {
        return mapping;
    }

    /*
    ----------------------Just making sure input client are in client list---------------
     */
    private EdgeClient getClientByID(String clientID) throws Exception {
        /*
         TODO: Can be improve/ultilize if use straight EdgeClient rather than clientID, maybe i'm wrong cuz we are using string regex to get info from JSON file.
         */
        for (EdgeClient client : this.clientList) {
            if (client.getId().equals(clientID)) {
                return client;
            }
        }
        throw new Exception("No client found");
    }
}
