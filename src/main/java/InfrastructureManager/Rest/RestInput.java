package InfrastructureManager.Rest;

import InfrastructureManager.EdgeClient;
import InfrastructureManager.EdgeNode;
import InfrastructureManager.Master;
import InfrastructureManager.MasterInput;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.*;

public class RestInput implements MasterInput {
    private static String command = "";
    private static ObjectMapper mapper = new ObjectMapper();


    public static Route registerClient = (Request request, Response response) -> {
        String clientAsString = request.body().replaceAll("\\s+", "");
        EdgeClient client = mapper.readValue(clientAsString, EdgeClient.class);
        command = "register_client " + clientAsString;
        //if everythings go right, response.status() = 200
        return response.status();
    };

    public static Route registerNode = (Request request, Response response) -> {
        Logger logger = LoggerFactory.getLogger(RestInput.class);
//        logger.info("request: {}\nresponse: {}",request.body(),response.body());
        String nodeAsString = request.body().replaceAll("\\s+", "");
        EdgeNode node = mapper.readValue(nodeAsString, EdgeNode.class);
        command = "register_node " + nodeAsString;

        return response.status();
    };

    public static Route assignClient = (Request request, Response response) -> {
        command = "assign_client " + request.params(":client_id").replaceAll("\\s+", "");
        return response.status();
    };

    public static Route disconnectClient = (Request request, Response response) -> {
        String disconnectBodyAsString = request.body().replaceAll("\\s+", "");
        /*
        response string should look like this (no space in JSON BODY)
        matchMaker disconnect_client {"id":"client1","disconnected_reason":"job_done"}
         */
        command = "disconnect_client " + disconnectBodyAsString;
        return response.status();
    };

    public static Route updateNode = (Request request, Response response) -> {
        String updatedNodeAsString = request.body().replaceAll("\\s+", "");
        EdgeNode updatedNode = mapper.readValue(updatedNodeAsString, EdgeNode.class);
        //get the node's ID that we want to update. Only update the one with the same ID please, y'all
        command = "update_node " + updatedNodeAsString;
        return response.status();
    };

    public static Route updateClient = (Request request, Response response) -> {
        String updatedClientAsString = request.body().replaceAll("\\s+", "");
        EdgeClient updatedClient = mapper.readValue(updatedClientAsString, EdgeClient.class);
        //get the node's ID that we want to update. Only update the one with the same ID please y'all
        String oldClientID = updatedClient.getId();
        command = "update_client " + updatedClientAsString;
        return response.status();
    };

    @Override
    public String read() throws Exception {
        Logger logger = LoggerFactory.getLogger(RestInput.class);
        if (command.isEmpty()) {
            throw new Exception("No command exception");
        }
        String executed = command;
        logger.info("executed command: {}", executed);
        /*
        the print out above look like this
        executed command: register_node {"id":"node1","ipAddress":"68.131.232.215:30968","connected":true,"resource":100,"network":100,"location":55}
         */
        command = "";
        return executed;
    }
}
