package InfrastructureManager.Modules.REST.Output;

import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;
import InfrastructureManager.ModuleManagement.PlatformOutput;
import InfrastructureManager.Modules.REST.Exception.Output.RESTOutputException;
import InfrastructureManager.Modules.REST.RESTModuleObject;
import InfrastructureManager.Modules.REST.RestServerRunner;
import spark.Request;
import spark.Response;
import spark.Route;

import static spark.Spark.get;

/**
 * Class that represents providing REST resources to be consumed by GET requests as an output for the platform.
 * <p>
 * Outputs of this type represent generic GET routes that can be customized regarding URL.
 * This customization includes the creation of routes that handle named parameters in the path (for example parameter name in /hello/:name).
 */
public class GETOutput extends RESTModuleObject implements PlatformOutput {

    private String toSend;
    protected final String URL;

    protected boolean isActivated; //To check if REST server is running

    /**
     * Spark route to handle incoming GET requests, reads from the last saved value and creates the resource
     */
    protected Route GETHandler = (Request request, Response response) -> {
        response.type("application/json");
        return this.toSend;
    };

    /**
     * Constructor, creates a generic GET output based on the path to create the resource
     *
     * @param module Owner module of the outpuut
     * @param name   Name of the output
     * @param URL    Path in which the GET handler will be set (Resource will be created)
     */
    public GETOutput(ImmutablePlatformModule module, String name, String URL) {
        super(module,name);
        this.toSend = "";
        this.URL = URL;
        this.isActivated = false;
    }

    /**
     * Based on processed responses from the inputs executes the different functionalities
     *
     * @param response Must be in the way "toGET COMMAND" and additionally:
     *                 - Json body of the resource as string (Ex. toGET resource {\"name\": \"example\"})
     * @throws RESTOutputException if an invalid or incomplete command is passed
     */
    @Override
    public void execute(String response) throws RESTOutputException {
        this.getLogger().debug(this.getName() + " - Executing diff functionalities based on the processed resp from the inputs" );
        String[] command = response.split(" ",3);
        if (command[0].equals("toGET")) {
            try {
                switch (command[1]) {
                    case "resource" -> addResource(command[2]);
                    default -> throw new RESTOutputException("Invalid command " + command[1] + " for REST output "
                    + this.getName());
                }
            } catch (IndexOutOfBoundsException e){
                throw new RESTOutputException("Arguments missing for command " + response + " to REST Output "
                + this.getName());
            }
        }
    }


    /**
     * Synchronize the thread with the RestServerRunner using a guarded block to ensure that the get handler is only activated if
     * the REST server is already running.
     * Only runs once at the beginning
     */
    public void activate() {
        try {
            RestServerRunner.serverCheck.acquire();
            get(this.URL, this.GETHandler);
            this.isActivated = true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            RestServerRunner.serverCheck.release();
        }
    }

    /**
     * Adds a given JSON body to be outputted.
     *
     * @param jsonBody JSON body that will be a GET resource
     */
    protected void addResource(String jsonBody) {
        this.toSend = jsonBody;
    }
}
