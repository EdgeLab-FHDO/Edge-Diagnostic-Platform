package InfrastructureManager.Modules.REST.Input;

import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;
import InfrastructureManager.Modules.REST.Exception.Input.UnsupportedJSONTypeException;
import InfrastructureManager.Modules.REST.RESTModuleObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Class to abstract a JSON object which has unknown parameters (Both type and value)
 */
public class UnknownJSONObject extends RESTModuleObject {

    private final JsonNode tree;
    private final String body;

    /**
     * Constructor of the class
     * @param jsonBody Body to create the object from
     * @throws JsonProcessingException If an error occurs while parsing
     */
    public UnknownJSONObject(ImmutablePlatformModule ownerModule, String jsonBody) throws JsonProcessingException {
        super(ownerModule);
        ObjectMapper mapper = new ObjectMapper();
        this.body = jsonBody;
        this.tree = mapper.readTree(this.body); //Create JSON node
    }

    /**
     * Get the string representation of a parameter's value in the passed json based only on the name of the parameter.
     * Right now, supports parameters with string, boolean, floating point and integer values (Not arrays or objects)
     * @param fieldName Name of a parameter in the JSON object
     * @return String representation of the value of the parameter
     * @throws IllegalArgumentException if the parameter type is not supported (Array, JSON object, null)
     */
    public String getValue(String fieldName) throws UnsupportedJSONTypeException {
        JsonNode node = tree.get(fieldName);
        if (node == null) {
            return null;
        }
        return switch (node.getNodeType()) {
            case STRING -> node.textValue();
            case BOOLEAN, NUMBER -> node.asText();
            default -> throw new UnsupportedJSONTypeException("Parameter is not a String, Boolean or Number value");
        };
    }

    /**
     * Get the json body of the object as string
     * @return Body as string
     */
    public String getBody() {
        return body;
    }
}
