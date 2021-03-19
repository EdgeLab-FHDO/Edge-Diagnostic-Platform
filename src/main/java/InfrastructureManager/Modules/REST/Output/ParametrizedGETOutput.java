package InfrastructureManager.Modules.REST.Output;

import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;
import InfrastructureManager.Modules.REST.Exception.Output.RESTOutputException;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;

/**
 * Subclass of {@link GETOutput} that represent outputs that handle resource creation when the defined
 * URL has path parameters included. This means, the resource created is to be binded with a specific parameter (e.g. "id").
 */
public class ParametrizedGETOutput extends GETOutput {

    private final String parameter;
    private final Map<String,String> info = new HashMap<>();

    /**
     * Constructor, creates a generic GET output based on the path to create the resource
     *
     * @param module    Owner module of this output
     * @param name      Name of the output
     * @param URL       Path in which the GET handler will be set (Resource will be created)
     * @param parameter Parameter which is present in the URL.
     */
    public ParametrizedGETOutput(ImmutablePlatformModule module, String name, String URL, String parameter) {
        super(module,name, URL);
        this.parameter = parameter;
    }

    /**
     * Based on processed responses from the inputs executes the different functionalities
     * @param response Must be in the way "toGET COMMAND" and additionally:
     *                 - Parameter to which the resource will be bound.
     *                 - Json body of the resource as string (Ex. toGET resource {\"name\": \"example\"})
     * @throws RESTOutputException if an invalid or incomplete command is passed.
     */
    @Override
    public void execute(String response) throws RESTOutputException {
        String[] command = response.split(" ",4);
        if (command[0].equals("toGET")) {
            try {
                switch (command[1]) {
                    case "resource" -> addResource(command[2], command[3]);
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
    @Override
    public void activate() {
        this.GETHandler = (Request request, Response response) -> {
            response.type("application/json");
            String parameterFromRequest = request.params(":" + this.parameter);
            if (this.info.containsKey(parameterFromRequest)) {
                response.status(200);
                response.body(this.info.get(parameterFromRequest));
            } else {
                response.status(404);
                response.body("No resource was found for parameter " + parameterFromRequest);
            }
            return response.body();
        };
        super.activate();
    }

    /**
     * Adds a given JSON body to be outputted.
     * @param parameter Parameter value to which the jsonBody is related
     * @param jsonBody JSON body that will be a GET resource
     */
    protected void addResource(String parameter, String jsonBody) {
        this.info.put(parameter, jsonBody);
    }
}
