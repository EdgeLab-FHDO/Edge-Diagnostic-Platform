package InfrastructureManager.MatchMaking;

import InfrastructureManager.EdgeClient;
import InfrastructureManager.EdgeNode;
import InfrastructureManager.MasterInput;
import InfrastructureManager.MasterOutput;
import InfrastructureManager.Rest.RestOutput;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.HashMap;
import java.util.Map;

public class MatchMaker implements MasterOutput {

    public static MatchMaker instance;
    private MatchMakingAlgorithm algorithm;
    private Map<EdgeClient, EdgeNode> matches;

    private MatchMaker() {
        this.matches = new HashMap<>();
    }

    public void setAlgorithm(MatchMakingAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    public void assign (EdgeClient client) {
        this.matches.put(client, this.algorithm.match(client));
    }

    public Route assignNode = (Request request, Response response) -> {
        EdgeClient client = new EdgeClient(request.body()); //TODO:Define body from application
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
