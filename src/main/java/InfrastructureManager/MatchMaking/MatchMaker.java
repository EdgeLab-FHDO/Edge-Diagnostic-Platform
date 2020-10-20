package InfrastructureManager.MatchMaking;

import InfrastructureManager.EdgeClient;
import InfrastructureManager.EdgeNode;
import InfrastructureManager.MasterInput;
import InfrastructureManager.MasterOutput;
import InfrastructureManager.Rest.RestOutput;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.HashMap;
import java.util.Map;

public class MatchMaker {

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
        this.matches.put(client, this.algorithm.match(client));
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

    public Route assignNode = (Request request, Response response) -> {
        EdgeClient client = this.mapper.readValue(request.body(), EdgeClient.class); //TODO:Define body from application
        this.assign(client);
        return response.status();
    };

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

    /*

    @Override
    public void out(String response) throws IllegalArgumentException {
        String[] command = response.split(" ");
        if (command[0].equals("matchMaking")) {
            try {
                switch (command[0]) {
                    case "sendNode":
                        nodeToRestResource(command[1]);
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid command for MatchMaker");
                }
            } catch (IndexOutOfBoundsException e) {
                throw new IllegalArgumentException("Arguments missing for command - RESTOutput");
            }
        }
    }*/
}
