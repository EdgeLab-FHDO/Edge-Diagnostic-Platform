package InfrastructureManager.NewREST;

import InfrastructureManager.Runner;
import spark.Spark;

import java.net.HttpURLConnection;
import java.net.URL;

import static spark.Spark.*;

public class RestServerRunner extends Runner {

    private final int port;
    private final String heartbeatPath = "/heartbeat";
    public static final Object ServerRunning =new Object();

    private static RestServerRunner restRunner = null;

    private RestServerRunner(String name, int port) {
        super(name);
        this.port = port;
    }

    /**
     * Starts the REST server on the defined port, creates the route for the heartbeat check.
     */
    private void startServer() {
        Spark.port(this.port);
        initExceptionHandler(Throwable::printStackTrace); //Print the exception is an error happens when starting
        get(heartbeatPath, (request, response) -> response.status());
    }

    /**
     * Wrapper method for starting the server, that checks whether the server is up or not before trying to start it
     * It also waits until the server is ready before returning (If the server was already running returns immediately)
     */
    private void startServerIfNotRunning() {
        if(!serverIsRunning()) {
            startServer();
        }
        awaitInitialization();
        synchronized (ServerRunning) {
            ServerRunning.notifyAll();
        }
    }

    /**
     * Sends an HTTP request to the defined heartbeat path, to check health of the server
     * @return true if the server is running normally (response 200), false otherwise
     */
    private boolean serverIsRunning(){
        try{
            String host = "localhost";
            HttpURLConnection con = (HttpURLConnection)new URL("http", host, port, heartbeatPath).openConnection();
            return con.getResponseCode()==200;
        }catch(Exception e){
            return false;
        }
    }

    /**
     * Method for stopping the static Spark instance
     */
    private void killServer() {
        Spark.stop();
        Spark.awaitStop();
    }

    /**
     * Method for getting the Singleton REST Server instance.
     * @throws IllegalStateException If `configure()` method has not been called yet
     */
    public static RestServerRunner getInstance() throws IllegalStateException {
        if(restRunner == null) {
            throw new IllegalStateException("Rest Server is not configured");
        }
        return restRunner;
    }

    /**
     * Configure name and port to the rest server singleton. This should be called before getting the instance (`getRestServerRunner`)
     */
    public static void configure(String name, int port) {
        if(restRunner == null) {
            restRunner = new RestServerRunner(name, port);
        }
    }

    @Override
    public void run() {
        this.startServerIfNotRunning();
        running = true;
    }

    @Override
    public void exit() {
        killServer();
        super.exit();
    }
}
