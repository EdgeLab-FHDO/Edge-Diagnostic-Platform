package DiagnosticsClient.Communication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ServerInformation {
    private final String ipAddress;
    private final int port;

    public ServerInformation(String response) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(response);
        String[] completeAddress = node.findValue("ipAddress").asText().split(":");
        this.ipAddress = completeAddress[0];
        this.port = Integer.parseInt(completeAddress[1]);
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public int getPort() {
        return port;
    }
}
