package InfrastructureManager.OldRest.Utility.Authentication;

import InfrastructureManager.NewREST.Authentication.RESTAuthenticator;
import InfrastructureManager.NewREST.Authentication.DummyAuthentication;
import spark.*;
import static spark.Spark.*;

public class AuthenticationManager {
    public static Filter authenticate = (Request request, Response response) -> {
        boolean authenticated;
        RESTAuthenticator authentication = new DummyAuthentication();
        authenticated = authentication.authenticate();
        if (!authenticated) {
            halt(401, "Request not authorized");
        }
    };
}
