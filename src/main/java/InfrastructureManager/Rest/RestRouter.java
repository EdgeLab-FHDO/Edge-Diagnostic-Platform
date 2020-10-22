package InfrastructureManager.Rest;

import InfrastructureManager.MatchMaking.MatchMaker;
import InfrastructureManager.Rest.Utility.Authentication.AuthenticationManager;
import spark.*;
import static spark.Spark.*;

public class RestRouter {

    private RestRouter(int port) {
        before("/*", AuthenticationManager.authenticate);
        path("/node", () -> {
            path("/test", () -> {
                post("/read/:input", RestInput.readParameterTest);
            });
            post("/execute/:command", RestInput.executeCommand);
            post("/register", RestInput.registerNode);
        });
        path("/client", () -> {
            post("/register", RestInput.registerClient);
            get("/get_node/:client_id", RestOutput.getInstance().sendNodeInfo);
        });
        get("/limit", RestOutput.getInstance().sendLimitInfo);
        get("/heartbeat", (request, response) -> {
            return response.status();
        });

    }

    public static RestRouter startRouter(int port) {
        Spark.port(port);
        return new RestRouter(port);
    }
}
