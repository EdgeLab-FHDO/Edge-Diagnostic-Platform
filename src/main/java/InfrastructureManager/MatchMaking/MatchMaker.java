package InfrastructureManager.MatchMaking;

import InfrastructureManager.EdgeClient;
import InfrastructureManager.EdgeNode;
import InfrastructureManager.MasterInput;
import InfrastructureManager.MasterOutput;
import InfrastructureManager.Rest.RestOutput;
import com.fasterxml.jackson.databind.ObjectMapper;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.HashMap;
import java.util.Map;

public class MatchMaker implements MasterOutput {

    public static MatchMaker instance;
    private MatchMakingAlgorithm algorithm;
    private Map<EdgeClient, EdgeNode> matches;
    private ObjectMapper mapper;

    private MatchMaker() {
        this.matches = new HashMap<>();
        this.algorithm = new RandomMatchMaking(); // For now
        this.mapper = new ObjectMapper();
    }

    public void setAlgorithm(MatchMakingAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    public void assign (EdgeClient client) {
        System.out.println("Client received : " + client.getId());
        this.matches.put(client, this.algorithm.match(client));
        System.out.println("Server assigned : " + this.matches.get(client).getId());
    }

    public Route assignNode = (Request request, Response response) -> {
        EdgeClient client = this.mapper.readValue(request.body(), EdgeClient.class); //TODO:Define body from application
        this.assign(client);
        return response.status();
    };

    public static MatchMaker getInstance() {
        if (instance == null) {
            instance = new MatchMaker();
        }
        return instance;
    }

    @Override
    public void out(String response) throws IllegalArgumentException {

    }
}
