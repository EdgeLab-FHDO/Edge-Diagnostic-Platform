package InfrastructureManager.NewREST;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UnknownJSONObject {

    private final JsonNode tree;
    Map<String,String> stringValues = new HashMap<>();
    Map<String,Integer> intValues = new HashMap<>();
    Map<String,Boolean> booleanValues = new HashMap<>();

    public UnknownJSONObject(String jsonBody, List<String> toParse) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        this.tree = mapper.readTree(jsonBody);
        for (String fieldName : toParse) {
            putValue(fieldName);
        }
    }

    public String getValue(String fieldName) {
        if (stringValues.containsKey(fieldName)) {
            return stringValues.get(fieldName);
        }
        if (intValues.containsKey(fieldName)) {
             return intValues.get(fieldName).toString();
        }
        if (booleanValues.containsKey(fieldName)) {
            return booleanValues.get(fieldName).toString();
        }
        else {
            return null;
        }
    }

    private void putValue(String searched) {
        JsonNode node = tree.get(searched);
        if (node == null) {
            System.out.println("Not found");
            return;
        }
        switch (node.getNodeType()) {
            case STRING:
                this.stringValues.put(searched,node.textValue());
                break;
            case BOOLEAN:
                this.booleanValues.put(searched, node.booleanValue());
                break;
            case NUMBER:
                this.intValues.put(searched,node.intValue());
                break;
            default:
                System.out.println("error");
        }
    }


}
