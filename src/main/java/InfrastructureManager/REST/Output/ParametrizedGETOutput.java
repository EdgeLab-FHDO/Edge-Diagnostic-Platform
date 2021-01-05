package InfrastructureManager.REST.Output;

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
     * @param path Path in which the GET handler will be set (Resource will be created)
     */
    public ParametrizedGETOutput(String name, String path, String parameter) {
        super(name, path);
        this.parameter = parameter;
    }

    @Override
    public void out(String response) throws IllegalArgumentException {
        String[] command = response.split(" ",4);
        if (command[0].equals("toGET")) {
            try {
                switch (command[1]) {
                    case "resource":
                        addResource(command[2], command[3]);
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid command for REST");
                }
            } catch (IndexOutOfBoundsException e){
                throw new IllegalArgumentException("Arguments missing for command - REST");
            }
        }
    }

    @Override
    protected void activate() {
        this.GETHandler = (Request request, Response response) -> {
            response.type("application/json");
            String parameterFromRequest = request.params(":" + this.parameter);
            return this.info.getOrDefault(parameterFromRequest,"");
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
