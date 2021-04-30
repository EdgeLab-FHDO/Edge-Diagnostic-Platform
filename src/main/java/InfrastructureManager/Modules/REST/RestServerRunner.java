package InfrastructureManager.Modules.REST;

import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;
import InfrastructureManager.Modules.REST.Authentication.DummyAuthentication;
import InfrastructureManager.Modules.REST.Authentication.RESTAuthenticator;
import InfrastructureManager.ModuleManagement.Runner;
import InfrastructureManager.Modules.REST.Exception.Server.ServerNotConfiguredException;
import spark.Filter;
import spark.Request;
import spark.Response;
import spark.Spark;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Semaphore;
import java.util.function.Consumer;

import static spark.Spark.*;

/**
 * Built-in REST Server of the platform.
 *
 * Implements a REST server based on configured routes, which defined URIs and callbacks to functions to generate responses.
 * The server, can be set-up to be exposed in any available port. although by default is configured in port 4657 by the
 * RESTModule
 *
 * Defined as a {@link Runner} for it to be executed in a separate thread on run-time.
 */
public class RestServerRunner extends Runner {

    private final int port;
    private final String heartbeatPath = "/heartbeat"; //Path where the heartbeat is defined to check if server is up (always in localhost)

    private final RESTAuthenticator authenticator; //For future authentication implementations
    private final Filter authenticationHandler; //Handler for authentication to the server
    private final Consumer<Exception> initExceptionHandler;

    public static final Semaphore serverCheck = new Semaphore(1);
    private static RestServerRunner restRunner = null; //Singleton implementation

    /**
     * Create a new server, private according to singleton implementation
     *
     * This method blocks until the server is started to finish the configuration.
     *
     * @param ownerModule Owner module of the server
     * @param name        Name of the server. Normally called "REST_SERVER".
     * @param port        Port where the server will be exposed
     * @throws InterruptedException If interrupted while blocked.
     */
    private RestServerRunner(ImmutablePlatformModule ownerModule, String name, int port) throws InterruptedException {
        super(ownerModule,name,null);
        serverCheck.acquire();
        this.port = port;
        this.authenticator = new DummyAuthentication();
        this.authenticationHandler = (Request request, Response response) -> {
            if (!authenticator.authenticate()) {
                this.getLogger().debug(this.getName(), "401 Request not authorized");
                halt(401, "Request not authorized");
            }
        };
        this.initExceptionHandler = e -> {
            e.printStackTrace();
            System.exit(-1);
        };
    }

    /**
     * Starts the REST server on the defined port, creates the route for the heartbeat check and performs the authentication.
     */
    private void startServer() {
        this.getLogger().debug(this.getName(), "Starting Server on port: " + this.port);
        Spark.port(this.port);
        initExceptionHandler(this.initExceptionHandler); //Print the exception is an error happens when starting
        before(this.authenticationHandler);
        this.getLogger().debug(this.getName(), "Server authentication passed");
        get(heartbeatPath, (request, response) -> response.status());
        this.getLogger().debug(this.getName(), "Heartbeat route created");
    }

    /**
     * Wrapper method for starting the server, that checks whether the server is up or not before trying to start it
     * It also waits until the server is ready before returning (If the server was already running returns immediately)
     */
    private void startServerIfNotRunning() {
        if(!serverIsRunning()) {
            this.getLogger().debug(this.getName(), "Server not running, restarting server");
            startServer();
            awaitInitialization();
            serverCheck.release();
        }
    }

    /**
     * Sends an HTTP request to the defined heartbeat path, to check health of the server
     *
     * @return true if the server is running normally (response 200), false otherwise
     */
    private boolean serverIsRunning(){
        try{
            this.getLogger().debug(this.getName(), "Sending heartbeat");
            String host = "localhost";
            HttpURLConnection con = (HttpURLConnection)new URL("http", host, port, heartbeatPath).openConnection();
            return con.getResponseCode()==200;
        }catch(Exception e){
            this.getLogger().debug(this.getName(), "Server not running normally");
            return false;
        }
    }

    /**
     * Method for stopping the static Spark instance (embedded web server)
     */
    private void killServer() {
        this.getLogger().debug(this.getName(), "Stopping the static Spark instance");
        Spark.stop();
        Spark.awaitStop();
    }

    /**
     * Method for getting the Singleton REST Server instance.
     *
     * @return The rest server instance
     * @throws ServerNotConfiguredException If the server has not been configured (with {@link #configure(ImmutablePlatformModule, String, int)})
     */
    public static RestServerRunner getInstance() throws ServerNotConfiguredException {
        if(restRunner == null) {
            throw new ServerNotConfiguredException("Rest Server is not configured");
        }
        return restRunner;
    }

    /**
     * Configure name and port to the rest server singleton.
     * If called more than once will just configure the server once and the other times just return.
     * This should be called before getting the instance with {@link #getInstance()}
     *
     * @param ownerModule Owner module of the server
     * @param name        Name of the server. Normally called "REST_SERVER".
     * @param port        Port where the server will be exposed.
     */
    public static void configure(ImmutablePlatformModule ownerModule,String name, int port) {
        if(restRunner == null) {
            try {
                restRunner = new RestServerRunner(ownerModule,name, port);
            } catch (InterruptedException e) {
                e.printStackTrace();
                restRunner = null;
            }
        }
    }

    /**
     * Start the rest server.
     */
    @Override
    public void run() {
        this.getLogger().debug(this.getName(), "Starting the rest server");
        this.startServerIfNotRunning();
        running = true;
    }

    /**
     * Kill the server and exit the thread.
     */
    @Override
    public void exit() {
        this.getLogger().debug(this.getName(), "Stopping the server and thread");
        killServer();
        super.exit();
    }
}
