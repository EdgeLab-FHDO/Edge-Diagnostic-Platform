package InfrastructureManager.NewREST;

import spark.Spark;

public class NewRouter {

    public static void startRouter() {
        Spark.port(4859);
    }
}
