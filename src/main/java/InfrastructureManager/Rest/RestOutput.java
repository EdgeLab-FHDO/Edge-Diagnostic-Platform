package InfrastructureManager.Rest;

import InfrastructureManager.Master;
import InfrastructureManager.MasterOutput;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import org.json.*;

public class RestOutput implements MasterOutput {

    private static RestOutput instance = null;

    private final Queue<String> output;
    private final Map<String,Double> limitNodes;

    public Route sendLimitInfo = (Request request, Response response) -> getLimitInfo(response);
    public Route getNode = (Request request, Response response) -> printResponse();

    private RestOutput() {
        this.output = new LinkedList<>();
        this.limitNodes = new LinkedHashMap<>();
    }
    public JSONObject printResponse() {
        JSONObject response = new JSONObject();

        if(output.isEmpty()) {
            response.put("content", "");
        } else {
            response.put("content", output.remove());
        }

        return response;
    }
    public String getLimitInfo(Response response) {
        ObjectMapper mapper = new ObjectMapper();
        String response_body = "";
        response.type("application/json");
        try {
            response_body = mapper.writeValueAsString(limitNodes);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return response_body;
    }

    @Override
    public void out(String response) {
        String[] command = response.split(" ");
        try {
            switch (command[0]) {
                case "rest":
                    output.add(command[1]);
                    break;
                case "limit":
                    addToLimitList(command[1], command[2]);
                    break;
                default:
            }
        } catch (IndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Arguments missing for command - RESTOutput");
        }

    }

    private void addToLimitList(String tag, String limit) {
        this.limitNodes.put(tag, Double.parseDouble(limit));
    }

    public static RestOutput getInstance() {
        if (instance == null) {
            instance = new RestOutput();
        }
        return instance;
    }
}