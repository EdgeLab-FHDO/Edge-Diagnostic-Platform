package InfrastructureManager.Rest;


import InfrastructureManager.Rest.Utility.Authentication.AuthenticationManager;
import InfrastructureManager.Rest.Utility.Authentication.MasterAuthentication;
import InfrastructureManager.Rest.Utility.Authentication.SampleAuthentication;
import spark.*;
import static spark.Spark.*;

public class RestRouter {
    public static void main(String[] args) {
        before("/*", AuthenticationManager.authenticate);
        path("/node", () -> {
            post("/command/:command", RestInput.readCommand);
        });
        path("/client", () -> {
            get("/:response", RestOutput.printResponse);
        });
    }
}
