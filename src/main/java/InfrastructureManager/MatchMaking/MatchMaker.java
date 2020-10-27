package InfrastructureManager.MatchMaking;

import InfrastructureManager.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MatchMaker implements MasterInput, MasterOutput {

    private MatchMakingAlgorithm algorithm;
    private final ObjectMapper mapper;
    private String command = "";

    private List<EdgeNode> nodeList;
    private List<EdgeClient> clientList;
    private Map<EdgeClient,EdgeNode> mapping;

    public MatchMaker() {
        this.algorithm = new RandomMatchMaking(); // For now
        this.mapper = new ObjectMapper();

        this.nodeList = new ArrayList<>();
        this.clientList = new ArrayList<>();
        this.mapping = new HashMap<>();
    }

    public void setAlgorithm(MatchMakingAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    private void assign (String clientID) {
        try {
            EdgeClient client = Master.getInstance().getClientByID(clientID);
            EdgeNode node = this.algorithm.match(client);
            if (node == null) {
                System.out.println("No node to assign");
            } else {
                this.mapping.put(client,node);
                command = "give_node " + client.getId() + " " + node.getId();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void out(String response) throws IllegalArgumentException {
        String[] command = response.split(" ");
        if (command[0].equals("matchMaker")) {
            try {
                switch (command[1]){
                    case "register_client" :
                        registerClient(command[2]);
                        break;
                    case "register_node" :
                        registerNode(command[2]);
                        break;
                    case "assign_client" :
                        assign(command[2]);
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid command for MatchMaker");
                }
            } catch (IndexOutOfBoundsException e) {
                throw new IllegalArgumentException("Arguments missing for command - MatchMaker");
            }
        }
    }

    private void registerClient(String clientAsString) {
        try {
            EdgeClient client = this.mapper.readValue(clientAsString, EdgeClient.class);
            this.clientList.add(client);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private void registerNode(String nodeAsString) {
        try {
            EdgeNode node = this.mapper.readValue(nodeAsString, EdgeNode.class);
            this.nodeList.add(node);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String read() throws Exception {
        if (command.isEmpty()) {
            throw new Exception("No command exception");
        }
        String toSend = command;
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
}
