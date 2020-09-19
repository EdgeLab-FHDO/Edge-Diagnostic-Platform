package InfrastructureManager.Rest;

import InfrastructureManager.MasterOutput;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.LinkedList;
import java.util.Queue;

import org.json.*;

public class RestOutput implements MasterOutput {
    private static Queue<String> output = new LinkedList<>();

    public static Route getNode = (Request request, Response response) -> printResponse();

    public static JSONObject printResponse() {
        JSONObject response = new JSONObject();

        if(output.isEmpty()) {
            response.put("content", "");
        } else {
            response.put("content", output.remove());
        }

        return response;
    };

    @Override
    public void out(String response) {
        String[] command = response.split(" ");
        if (command[0].equals("rest")) {
            try {
                output.add(command[1]);
            } catch (IndexOutOfBoundsException e) {
                throw new IllegalArgumentException("Arguments missing for command - RESTOutput");
            }
        }
    }
}