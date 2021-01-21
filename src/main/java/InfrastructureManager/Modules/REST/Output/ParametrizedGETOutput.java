package InfrastructureManager.Modules.REST.Output;

import InfrastructureManager.Modules.REST.Exception.Output.RESTOutputException;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;

public class ParametrizedGETOutput extends GETOutput {

    private final String parameter;
    private final Map<String,String> info = new HashMap<>();

    /**
     * Constructor, creates a generic GET output based on the path to create the resource
     * @param name Name of the output
     * @param URL Path in which the GET handler will be set (Resource will be created)
     */
    public ParametrizedGETOutput(String name, String URL, String parameter) {
        super(name, URL);
        this.parameter = parameter;
    }

    @Override
    protected void out(String response) throws RESTOutputException {
        String[] command = response.split(" ",4);
        if (command[0].equals("toGET")) {
            try {
                switch (command[1]) {
                    case "resource" -> addResource(command[2], command[3]);
                    default -> throw new RESTOutputException("Invalid command " + command[1] + " for REST output "
                            + this.getName());
                }
            } catch (IndexOutOfBoundsException e){
                throw new RESTOutputException("Arguments missing for command" + response + " to REST Output "
                        + this.getName());
            }
        }
    }

    @Override
    protected void activate() {
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

    protected void addResource(String parameter, String jsonBody) {
        if (!this.isActivated) {
            this.activate();
        }
        this.info.put(parameter, jsonBody);
    }
}
