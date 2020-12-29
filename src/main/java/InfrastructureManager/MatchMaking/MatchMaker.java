package InfrastructureManager.MatchMaking;

import InfrastructureManager.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;


public class MatchMaker extends MasterOutput implements MasterInput {

    private MatchMakingAlgorithm algorithm;
    private final ObjectMapper mapper;
    private String command = "";

    private final List<EdgeNode> nodeList;
    private final List<EdgeClient> clientList;
    private final Map<EdgeClient,EdgeNode> mapping;

    private final Semaphore readingLock;

    public MatchMaker(String name, String algorithmType) {
        super(name);
        setAlgorithmFromString(algorithmType);
        this.mapper = new ObjectMapper();

        this.nodeList = new ArrayList<>();
        this.clientList = new ArrayList<>();
        this.mapping = new HashMap<>();

        this.readingLock = new Semaphore(0); // Binary semaphore, starts without permits so it will block until a request is made
    }

    public void setAlgorithmFromString(String algorithmType) {
        switch (algorithmType.toLowerCase()) {
            case "random" :
                setAlgorithm(new RandomMatchMaking());
                break;
            default:
                throw new IllegalArgumentException("Invalid type for MatchMaker");
        }
    }

    private void setAlgorithm(MatchMakingAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    private void assign (String clientID) {
        try {
            EdgeClient client = this.getClientByID(clientID);
            EdgeNode node = this.algorithm.match(client, this.nodeList);
            if (node == null) {
                System.out.println("No node to assign");
            } else {
                this.mapping.put(client,node);
                String nodeAsJSON = this.mapper.writeValueAsString(node);
                command = "give_node " + client.getId() + " " + nodeAsJSON;
                readingLock.release();
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
    public String read() throws InterruptedException {
        String toSend = getReading();
        command = "";
        return toSend;
    }

    private String getReading() throws InterruptedException {
        readingLock.acquire();
        return this.command;
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

    private EdgeClient getClientByID(String clientID) throws Exception {
        for (EdgeClient client : this.clientList) {
            if (client.getId().equals(clientID)) {
                return client;
            }
        }
        throw new Exception("No client found");
    }
}
