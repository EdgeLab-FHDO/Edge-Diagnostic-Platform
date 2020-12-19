package InfrastructureManager.REST.Output;

import InfrastructureManager.MasterOutput;
import InfrastructureManager.REST.RestServerRunner;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.*;

import static spark.Spark.get;

/**
 * Class to represent a MasterOutput that generates a REST resource to be consumed by GET requests
 * given the path in which the resource will be available and the json body of the desired response
 */
public class GETOutput extends MasterOutput {

    private Queue<String> toSend;//FIFO Queue to store data
    protected final String path;

    protected boolean isActivated; //To check if REST server is running

    /**
     * Spark route to handle incoming GET requests, reads from the defined queue and creates the resource
     */
    protected Route GETHandler = (Request request, Response response) -> {
        response.type("application/json");
        try {
            return this.toSend.remove();
        } catch (NoSuchElementException e) {
            return "";
        }
    };

    /**
     * Constructor, creates a generic GET output based on the path to create the resource
     * @param name Name of the output
     * @param path Path in which the GET handler will be set (Resource will be created)
     */
    public GETOutput(String name, String path) {
        super(name);
        this.toSend = new ArrayDeque<>();
        this.path = path;
        this.isActivated = false;
    }

    /**
     * Based on a command from the master creates a rest resource with a path and a json body
     * @param response Must be in the way "toGET command" where command can be:
     *                 - resource: Plus json body as string (Ex. toGET resource {\"name\": \"example\"})
     * @throws IllegalArgumentException If the command is not defined or is missing arguments
     */
    @Override
    public void out(String response) throws IllegalArgumentException {
        String[] command = response.split(" ",3);
        if (command[0].equals("toGET")) {
            try {
                switch (command[1]) {
                    case "resource":
                        addResource(command[2]);
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid command for REST");
                }
            } catch (IndexOutOfBoundsException e){
                throw new IllegalArgumentException("Arguments missing for command - REST");
            }
        }
    }


    /**
     * Synchronize the thread with the RestServerRunner using a guarded block to ensure that the get handler is only activated if
     * the REST server is already running.
     * Only runs once at the beginning
     */
    protected void activate() {
        try {
            while (!RestServerRunner.getInstance().isRunning()) { //Wait for the REST server to run
                synchronized (RestServerRunner.ServerRunning) {
                    RestServerRunner.ServerRunning.wait();
                }
            }
            get(this.path, this.GETHandler);
            this.isActivated = true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a given JSON body to the queue of the output.
     * Checks first if the route has been created (Output is activated)
     * @param jsonBody JSON body that will be a GET resource
     */
    protected void addResource(String jsonBody) {
        if (!this.isActivated) {
            this.activate();
        }
        this.toSend.offer(jsonBody);
    }
}
