package InfrastructureManager.Rest;

import InfrastructureManager.EdgeClient;
import InfrastructureManager.EdgeNode;
import InfrastructureManager.Master;
import InfrastructureManager.MasterInput;
import com.fasterxml.jackson.databind.ObjectMapper;
import spark.*;

public class RestInput implements MasterInput {
    private static String command = "";
    private static ObjectMapper mapper = new ObjectMapper();

    public static Route readParameterTest = (Request request, Response response) -> request.params(":input");

    /**
     * Execute route for POST request that only sends one way data to the Master
     * Returns true if the request is succesful with code 200
     */
    public static Route executeCommand = (Request request, Response response) -> {
        command = request.params(":command").replaceAll("\\s+","");
        return response.status();
    };

    public static Route registerClient = (Request request, Response response) -> {
        String clientAsString = request.body().replaceAll("\\s+","");
        EdgeClient client = mapper.readValue(clientAsString, EdgeClient.class);
        Master.getInstance().addClient(client);
        command = "register_client " + clientAsString;
        return response.status();
    };

    public static Route registerNode = (Request request, Response response) -> {
        String nodeAsString = request.body().replaceAll("\\s+","");
        EdgeNode node = mapper.readValue(nodeAsString, EdgeNode.class);
        Master.getInstance().addNode(node);
        command = "register_node " + nodeAsString;
        return response.status();
    };

    public static Route assignClient = (Request request, Response response) -> {
      command = "assign_client " + request.params(":client_id").replaceAll("\\s+","");
      return response.status();
    };

    @Override
    public String read() throws Exception {
        if(command.isEmpty()) {
           throw new Exception("No command exception");
        }
        String executed = command;
        command = "";
        return executed;
    }
}
