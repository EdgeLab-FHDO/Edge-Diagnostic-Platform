package InfrastructureManager.Rest;

import InfrastructureManager.Master;
import InfrastructureManager.MasterOutput;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.LinkedList;
import java.util.Queue;

import org.json.*;

public class RestOutput implements MasterOutput {

    private static RestOutput instance = null;

    private Queue<String> output = new LinkedList<>();

    public Route sendLimitInfo = (Request request, Response response) -> getLimitInfo(response);
    public Route getNode = (Request request, Response response) -> printResponse();

    public JSONObject printResponse() {
        JSONObject response = new JSONObject();

        if(output.isEmpty()) {
            response.put("content", "");
        } else {
            response.put("content", output.remove());
        }

        return response;
    }
    public JSONObject getLimitInfo(Response response) {
        response.type("application/json");
        JSONObject response_body = new JSONObject();
        if(output.isEmpty()) {
            response_body.put("content", "");
        } else {
            response_body.put("content", output.remove());
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
                    output.add(command[1]);
                    break;
                default:
            }
        } catch (IndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Arguments missing for command - RESTOutput");
        }

    }

    public static RestOutput getInstance() {
        if (instance == null) {
            instance = new RestOutput();
        }
        return instance;
    }
}