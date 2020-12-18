package InfrastructureManager.REST.Input;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Class to abstract a JSON object which has unknown parameters (Both type and value)
 */
public class UnknownJSONObject {

    private final JsonNode tree;
    private final String body;

    /**
     * Constructor of the class
     * @param jsonBody Body to create the object from
     * @throws JsonProcessingException If an error occurs while parsing
     */
    public UnknownJSONObject(String jsonBody) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        this.body = jsonBody;
        this.tree = mapper.readTree(this.body); //Create JSON node
    }

    /**
     * Get the string representation of a parameter's value in the passed json based only on the name of the parameter.
     * Right now, supports parameters with string, boolean, floating point and integer values (Not arrays or objects)
     * @param fieldName Name of a parameter in the JSON object
     * @return String representation of the value of the parameter
     */
    public String getValue(String fieldName) {
        JsonNode node = tree.get(fieldName);
        if (node == null) {
            return null;
        }
        switch (node.getNodeType()) {
            case STRING:
                return node.textValue();
            case BOOLEAN:
            case NUMBER:
                return node.asText();
            default:
                //TODO: Throw exception instead
                return null;
        }
    }

    /**
     * Get the json body of the object as string
     * @return Body as string
     */
    public String getBody() {
        return body;
    }
}
