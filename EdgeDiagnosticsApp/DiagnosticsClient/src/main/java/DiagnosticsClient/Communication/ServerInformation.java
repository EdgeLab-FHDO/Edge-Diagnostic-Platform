package DiagnosticsClient.Communication;

import DiagnosticsClient.Communication.Exception.JSONException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ServerInformation {
    private final String ipAddress;
    private final int port;

    public ServerInformation(String response) throws JSONException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node;
        try {
            node = mapper.readTree(response);
            this.ipAddress = node.findValue("address").asText();
            this.port = node.findValue("port").asInt();
        } catch (JsonProcessingException e) {
            throw new JSONException("Error while parsing server information", e);
        }
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public int getPort() {
        return port;
    }
}
