package InfrastructureManager.Rest;

import InfrastructureManager.Rest.Utility.Authentication.AuthenticationManager;
import spark.*;
import static spark.Spark.*;

public class RestRouter {
    private RestRouter(int port) {
        before("/*", AuthenticationManager.authenticate);
        path("/node", () -> {
            post("/read/:command", RestInput.readCommand);
            post("/execute/:command", RestInput.executeCommand);
        });
        path("/client", () -> {
            get("/:response", RestOutput.printResponse);
        });
        get("/heartbeat", (request, response) -> {
            return response.status();
        });
    }

    public static RestRouter startRouter(int port) {
        Spark.port(port);
        return new RestRouter(port);
    }
}
