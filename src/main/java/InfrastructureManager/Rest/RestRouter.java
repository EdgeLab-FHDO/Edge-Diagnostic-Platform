package InfrastructureManager.Rest;

import InfrastructureManager.Rest.Utility.Authentication.AuthenticationManager;
import spark.Spark;

import static spark.Spark.*;

public class RestRouter {
    /*
    Input for POST are configured here.
     */

    private RestRouter(int port) {
        try {
            before("/*", AuthenticationManager.authenticate);
            path("/node", () -> {
                post("/register", RestInput.registerNode);
                get("/limit", RestOutput.getInstance().sendLimitInfo);
                put("/update",RestInput.updateNode);
            });
            path("/client", () -> {
                post("/register", RestInput.registerClient);
                post("/assign/:client_id", RestInput.assignClient);
                //use getInstance() when output data ????
                get("/get_node/:client_id", RestOutput.getInstance().sendNodeInfo);
            });

            get("/heartbeat", (request, response) -> {
                return response.status();
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static RestRouter startRouter(int port) {
        Spark.port(port);
        return new RestRouter(port);
    }
}
