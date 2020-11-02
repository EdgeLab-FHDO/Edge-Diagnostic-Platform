package InfrastructureManager.Rest;

import InfrastructureManager.EdgeClient;
import InfrastructureManager.EdgeNode;
import InfrastructureManager.Master;
import InfrastructureManager.MasterOutput;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.*;

public class RestOutput extends MasterOutput {

    private static RestOutput instance = null;

    private Queue<String> output;
    private Map<String,String> limitNodes;
    private Map<String,String> nodesToSend;
    private final ObjectMapper mapper;

    public Route sendLimitInfo = (Request request, Response response) -> getLimitInfo(response);

    public Route sendNodeInfo = (Request request, Response response) -> {
        String clientID = request.params(":client_id").replaceAll("\\s+","");
        response.type("application/json");
        String response_body = this.nodesToSend.get(clientID);
        return response_body;
    };

    private RestOutput(String name) {
        super(name);
        this.mapper = new ObjectMapper();
        this.output = new LinkedList<>();
        this.limitNodes = new LinkedHashMap<>();
        this.nodesToSend = new HashMap<>();
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
        if (command[0].equals("restOut")) {
            try {
                switch (command[1]) {
                    case "sendNode":
                        createNodeResource(command[2], command[3]);
                        break;
                    case "limit":
                        String period = command.length > 4 ? command[4] : "100000";
                        addToLimitList(command[2], command[3],period);
                        break;
                    default:
                }
            } catch (IndexOutOfBoundsException e) {
                throw new IllegalArgumentException("Arguments missing for command - RESTOutput");
            }
        }

    }

    private void addToLimitList(String tag, String limit, String period_ms) {
        int quota_ms = (int) (Double.parseDouble(limit) * Integer.parseInt(period_ms));
        this.limitNodes.put(tag, quota_ms + "_" + period_ms);
    }

    public static void setInstanceName(String name) {
        if (instance == null) {
            instance = new RestOutput(name);
        }
    }

    public static RestOutput getInstance() {
        if (instance == null) {
            setInstanceName("rest_out"); //Default name because is a singleton
        }
        return instance;
    }

    public void resetOutput() {
        this.output = new LinkedList<>();
        this.limitNodes = new LinkedHashMap<>();
    }

    private void createNodeResource(String clientID, String nodeID) {
        try {
            EdgeClient client = Master.getInstance().getClientByID(clientID);
            EdgeNode node = Master.getInstance().getNodeByID(nodeID);
            String nodeAsJSON = this.mapper.writeValueAsString(node);
            this.nodesToSend.put(client.getId(), nodeAsJSON);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}