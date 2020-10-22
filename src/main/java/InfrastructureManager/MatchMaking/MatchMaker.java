package InfrastructureManager.MatchMaking;

import InfrastructureManager.*;
import InfrastructureManager.Rest.RestOutput;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class MatchMaker implements MasterOutput {

    public static MatchMaker instance;
    private MatchMakingAlgorithm algorithm;
    private Map<EdgeClient, EdgeNode> matches;
    private ObjectMapper mapper;
    private String JSONtoSend;

    private MatchMaker() {
        this.matches = new HashMap<>();
        this.algorithm = new RandomMatchMaking(); // For now
        this.mapper = new ObjectMapper();
    }

    public void setAlgorithm(MatchMakingAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    private void assign (EdgeClient client) {
        System.out.println("Client received : " + client.getId());
        EdgeNode node = this.algorithm.match(client);
        if (node == null) {
            System.out.println("No node to assign");
        } else {
            this.matches.put(client, this.algorithm.match(client));
        }
        System.out.println("Server assigned : " + this.matches.get(client).getId());
    }

    private EdgeClient registeredClientFromID (String id) {
        for (EdgeClient client : this.matches.keySet()) {
            if (client.getId().equals(id)) {
                return client;
            }
        }
        return null;
    }

    private void nodeToRestResource(String clientID) {
        EdgeNode node = this.matches.get(registeredClientFromID(clientID));
        try {
            this.JSONtoSend = this.mapper.writeValueAsString(node);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public Route sendNodeInfo = (Request request, Response response) -> {
        String id = request.params(":client_id").replaceAll("\\s+","");
        nodeToRestResource(id);
        response.type("application/json");
        String response_body = this.JSONtoSend;
        JSONtoSend = "";
        return response_body;
    };

    public static MatchMaker getInstance() {
        if (instance == null) {
            instance = new MatchMaker();
        }
        return instance;
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
                    default:
                        throw new IllegalArgumentException("Invalid Command for MatchMaker Output");
                }
            } catch (IndexOutOfBoundsException e) {
                throw new IllegalArgumentException("Arguments missing for command - MatchMaker");
            }
        }
    }

    private void registerClient(String clientAsString) {
        try {
            EdgeClient client = this.mapper.readValue(clientAsString, EdgeClient.class);
            Master.getInstance().addClient(client); //Add the client to the list
            assign(client); //Perform MatchMaking
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private void registerNode(String nodeAsString) {
        try {
            EdgeNode node = this.mapper.readValue(nodeAsString, EdgeNode.class);
            Master.getInstance().addNode(node);
            System.out.println("Node received: " + node.toString());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
