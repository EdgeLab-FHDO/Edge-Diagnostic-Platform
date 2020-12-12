package InfrastructureManager.NewREST;

import spark.Spark;

public class NewRouter {

    public static void startRouter(int port) {
        Spark.port(port);
    }
}
