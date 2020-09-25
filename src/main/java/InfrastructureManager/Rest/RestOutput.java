package InfrastructureManager.Rest;

import InfrastructureManager.Master;
import InfrastructureManager.MasterOutput;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class RestOutput implements MasterOutput {

    private static RestOutput instance = null;

    private Queue<String> output;
    private Map<String,String> limitNodes;
    private final ObjectMapper mapper;

    public Route sendLimitInfo = (Request request, Response response) -> getLimitInfo(response);
    public Route getNode = (Request request, Response response) -> printResponse();

    private RestOutput() {
        this.mapper = new ObjectMapper();
        this.output = new LinkedList<>();
        this.limitNodes = new LinkedHashMap<>();
    }
    public ObjectNode printResponse() {
        ObjectNode response_body = mapper.createObjectNode();
        if(output.isEmpty()) {
            response_body.put("content", "");
        } else {
            response_body.put("content", output.remove());
        }
        return response_body;
    }

    public String getLimitInfo(Response response) {
        response.type("application/json");
        String response_body = "";
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
                    String period = command.length > 3 ? command[3] : "100000";
                    addToLimitList(command[1], command[2],period);
                    break;
                default:
            }
        } catch (IndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Arguments missing for command - RESTOutput");
        }

    }

    private void addToLimitList(String tag, String limit, String period_ms) {
        int quota_ms = (int) (Double.parseDouble(limit) * Integer.parseInt(period_ms));
        this.limitNodes.put(tag, quota_ms + "_" + period_ms);
    }

    public static RestOutput getInstance() {
        if (instance == null) {
            instance = new RestOutput();
        }
        return instance;
    }

    public void resetOutput() {
        this.output = new LinkedList<>();
        this.limitNodes = new LinkedHashMap<>();
    }
}