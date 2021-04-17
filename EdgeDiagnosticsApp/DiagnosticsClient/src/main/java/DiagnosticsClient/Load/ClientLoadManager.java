package DiagnosticsClient.Load;

import Communication.Exception.RESTClientException;
import DiagnosticsClient.Communication.ClientPlatformConnection;
import DiagnosticsClient.Communication.ServerInformation;
import DiagnosticsClient.Control.BufferInformation;
import DiagnosticsClient.Load.Exception.LoadSendingException;
import DiagnosticsClient.Load.LoadTypes.DiagnosticsLoad;
import DiagnosticsClient.Load.TCP.TCPLoadSender;
import DiagnosticsClient.Load.UDP.UDPLoadSender;
import LoadManagement.BasicLoadManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.List;

public class ClientLoadManager extends BasicLoadManager {

    private final String address;
    private final int port;
    private List<Double> latencyMeasurements;
    private final ClientPlatformConnection connection;
    private ClientSocketOptions options;
    private LoadSender sender;

    public ClientLoadManager(ClientPlatformConnection connection, ServerInformation serverInformation) {
        this.address = serverInformation.getIpAddress();
        this.port = serverInformation.getPort();
        this.connection = connection;
        this.latencyMeasurements = new ArrayList<>();
    }

    @Override
    public void setConnectionType(ConnectionType connectionType) {
        super.setConnectionType(connectionType);
        this.sender = getSender(connectionType);
    }

    public void setSocketOptions(ClientSocketOptions options) {
        this.options = options;
    }

    public void sendLoad(DiagnosticsLoad load, BufferInformation bufferInformation) throws LoadSendingException {
        if (this.options != null) sender.changeSocketConfiguration(this.options);
        sender.send(load, bufferInformation);
    }

    public void reportMeasurements(String experimentName) throws RESTClientException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode result = mapper.createObjectNode();

        String latencyString = formatMeasurements();
        result.put("path","LatencyReports/" + experimentName + "_results.txt");
        result.put("content", latencyString);
        String body = mapper.writeValueAsString(result);
        connection.sendMeasurements(body);
        this.latencyMeasurements = new ArrayList<>();
        sender.setLatencyMeasurements(latencyMeasurements);
    }

    private LoadSender getSender(ConnectionType connectionType) {
        return switch (connectionType) {
            case TCP -> new TCPLoadSender(address,port,latencyMeasurements);
            case UDP -> new UDPLoadSender(address,port,latencyMeasurements);
        };
    }

    private String formatMeasurements() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < latencyMeasurements.size(); i++) {
            result.append(i);
            result.append(',');
            result.append(latencyMeasurements.get(i));
            result.append('\n');
        }
        return result.toString();
    }



}
