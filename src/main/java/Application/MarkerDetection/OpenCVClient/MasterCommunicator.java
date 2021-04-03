package Application.MarkerDetection.OpenCVClient;

import java.io.IOException;

import Application.Utilities.RESTHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

public class MasterCommunicator {
    //TODO consider moving url to configuration
    public String url;
    private final RESTHandler handler;

    public MasterCommunicator(String masterUrl) {
        url = masterUrl;
        this.handler = new RESTHandler();
    }

    public String getServer() throws InterruptedException, IOException {
        String getRequestBody = handler.sendGetRequest(url);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(getRequestBody);
        String serverInformation = node.findValue("ipAddress").asText();

        return serverInformation;
    }
}