package DiagnosticsClient.Load;

import DiagnosticsClient.Communication.ClientPlatformConnection;
import DiagnosticsClient.Load.Exception.LoadSendingException;
import DiagnosticsClient.Communication.ServerInformation;
import DiagnosticsClient.Load.LoadTypes.DiagnosticsLoad;
import DiagnosticsClient.Load.TCP.TCPLoadSender;
import DiagnosticsClient.Load.UDP.UDPLoadSender;
import LoadManagement.BasicLoadManager;
import Communication.Exception.RESTClientException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ClientLoadManager extends BasicLoadManager {

    private final String address;
    private final int port;
    private Map<Integer, Long> latencyMeasurements;
    private final ClientPlatformConnection connection;
    private ClientSocketOptions options;
    private String reportFilePath;
    private int fileCounter;

    public ClientLoadManager(ClientPlatformConnection connection, ServerInformation serverInformation) {
        this.address = serverInformation.getIpAddress();
        this.port = serverInformation.getPort();
        this.connection = connection;
        this.reportFilePath = "";
        this.fileCounter = 0;
    }

    public void setSocketOptions(ClientSocketOptions options) {
        this.options = options;
    }

    public void sendLoad(DiagnosticsLoad load) throws LoadSendingException {
        LoadSender sender = getSender();
        if (this.options != null) sender.changeSocketConfiguration(this.options);
        sender.send(load);
        reportFilePath = "load" + fileCounter + ".txt";
        fileCounter++;
    }

    public void reportMeasurements() throws RESTClientException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode result = mapper.createObjectNode();

        String latencyString = formatMeasurements();
        result.put("path","LatencyReports/" + reportFilePath);
        result.put("content", latencyString);
        String body = mapper.writeValueAsString(result);
        connection.sendMeasurements(body);
    }

    private LoadSender getSender() {
        latencyMeasurements = new HashMap<>();
        return switch (this.getConnectionType()) {
            case TCP -> new TCPLoadSender(address,port,latencyMeasurements);
            case UDP -> new UDPLoadSender(address,port,latencyMeasurements);
        };
    }

    private String formatMeasurements() {
        return latencyMeasurements.entrySet().stream()
                .map(e -> e.toString().replaceAll("=",",").trim())
                .collect(Collectors.joining(";"));
    }


}
