package Application.MarkerDetection.OpenCVClient;

import java.io.IOException;

import Application.Utilities.RESTHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

public class MasterCommunicator {
    //TODO consider moving url to configuration
    private String getServerUrl;
    private String disconnectUrl;
    private String disconnectBody;
    private final RESTHandler handler;
    private final OpenCVClientOperator operator;

    public MasterCommunicator(OpenCVClientOperator operator, String getServerUrl, String disconnectUrl, String disconnectBody) {
        this.operator = operator;
        this.getServerUrl = getServerUrl;
        this.disconnectUrl = disconnectUrl;
        this.disconnectBody = disconnectBody;
        this.handler = new RESTHandler();
    }

    public String getServer() throws InterruptedException, IOException {
        String getRequestBody = handler.sendGetRequest(getServerUrl);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(getRequestBody);
        // map to object
        // separate getIP to another method
        String serverInformation = node.findValue("ipAddress").asText();

        return serverInformation;
    }

    public void disconnectServer() throws IOException, InterruptedException {
        operator.clearTcpInformation();
        handler.sendPostRequest(disconnectUrl, disconnectBody);
    }
}