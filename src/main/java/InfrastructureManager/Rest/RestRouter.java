package InfrastructureManager.Rest;

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
        });
        path("/client", () -> {
            get("/get_node", RestOutput.getNode);
        });
        get("/limit", RestOutput.sendLimitInfo);
        get("/heartbeat", (request, response) -> {
            return response.status();
        });
    }

    public static RestRouter startRouter(int port) {
        Spark.port(port);
        return new RestRouter(port);
    }
}
