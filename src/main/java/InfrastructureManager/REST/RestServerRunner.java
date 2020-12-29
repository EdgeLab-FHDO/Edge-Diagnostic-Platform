package InfrastructureManager.REST;

import InfrastructureManager.REST.Authentication.DummyAuthentication;
import InfrastructureManager.REST.Authentication.RESTAuthenticator;
import InfrastructureManager.Runner;
import spark.Filter;
import spark.Request;
import spark.Response;
import spark.Spark;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Semaphore;

import static spark.Spark.*;

public class RestServerRunner extends Runner {

    private final int port;
    private final String heartbeatPath = "/heartbeat"; //Path where the hearbeath is defined to check if server is up (always in localhost)

    private final RESTAuthenticator authenticator; //For future authentication implementations
    private final Filter authenticationHandler; //Handler for authentication to the server

    public static final Semaphore serverCheck = new Semaphore(1);
    private static RestServerRunner restRunner = null; //Singleton implementation

    /**
     * Create a new server, private according to singleton implementation
     * @param name Name of the output as defined in MasterOutput abstract class
     * @param port Port where the server will be exposed
     */
    private RestServerRunner(String name, int port) throws InterruptedException {
        super(name);
        serverCheck.acquire();
        this.port = port;
        this.authenticator = new DummyAuthentication();
        this.authenticationHandler = (Request request, Response response) -> {
            if (!authenticator.authenticate()) {
                halt(401, "Request not authorized");
            }
        };
    }

    /**
     * Starts the REST server on the defined port, creates the route for the heartbeat check and performs the authentication.
     */
    private void startServer() {
        Spark.port(this.port);
        initExceptionHandler(Throwable::printStackTrace); //Print the exception is an error happens when starting
        before(this.authenticationHandler);
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
        serverCheck.release();
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
     * Method for stopping the static Spark instance (embedded web server)
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
     * Configure name and port to the rest server singleton.
     * If called more than once will just configure the server once and the other times just return.
     * This should be called before getting the instance (`getRestServerRunner`)
     */
    public static void configure(String name, int port) {
        if(restRunner == null) {
            try {
                restRunner = new RestServerRunner(name, port);
            } catch (InterruptedException e) {
                e.printStackTrace();
                restRunner = null;
            }
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
