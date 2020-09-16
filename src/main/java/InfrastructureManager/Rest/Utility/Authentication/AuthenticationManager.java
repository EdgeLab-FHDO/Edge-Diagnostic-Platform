package InfrastructureManager.Rest.Utility.Authentication;

import spark.*;
import static spark.Spark.*;

public class AuthenticationManager {
    public static Filter authenticate = (Request request, Response response) -> {
        boolean authenticated;
        MasterAuthentication authentication = new SampleAuthentication();
        authenticated = authentication.authenticate();
        if (!authenticated) {
            halt(401, "Request not authorized");
        }
    };
}
