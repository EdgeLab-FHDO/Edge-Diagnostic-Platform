package InfrastructureManager.REST.Input;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class UnknownJSONObject {

    private final JsonNode tree;

    public UnknownJSONObject(String jsonBody) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        this.tree = mapper.readTree(jsonBody);
    }

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
                return null;
        }
    }
}
