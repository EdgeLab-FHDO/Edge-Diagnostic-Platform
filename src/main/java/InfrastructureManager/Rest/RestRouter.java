package InfrastructureManager.Rest;

import InfrastructureManager.Rest.Utility.Authentication.AuthenticationManager;
import spark.*;
import static spark.Spark.*;

public class RestRouter {
    private RestRouter(int port) {
        before("/*", AuthenticationManager.authenticate);
        path("/node", () -> {
            post("/command/:command", RestInput.readCommand);
        });
        path("/client", () -> {
            get("/:response", RestOutput.printResponse);
        });
        get("/heartbeat", ((request, response) -> {
            response.status(200);
            return null;
        }));
    }

    public static RestRouter startRouter(int port) {
        Spark.port(port);
        return new RestRouter(port);
    }
}
