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

import java.util.HashMap;
import java.util.Map;

public class ClientLoadManager extends BasicLoadManager {

    private final String address;
    private final int port;
    private Map<Integer, Long> latencyMeasurements;
    private final ClientPlatformConnection connection;
    private ClientSocketOptions options;

    public ClientLoadManager(ClientPlatformConnection connection, ServerInformation serverInformation) {
        this.address = serverInformation.getIpAddress();
        this.port = serverInformation.getPort();
        this.connection = connection;
    }

    public void setSocketOptions(ClientSocketOptions options) {
        this.options = options;
    }

    public void sendLoad(DiagnosticsLoad load) throws LoadSendingException {
        LoadSender sender = getSender();
        if (this.options != null) sender.changeSocketConfiguration(this.options);
        sender.send(load);
    }

    public void reportMeasurements() throws JsonProcessingException, RESTClientException {
        ObjectMapper mapper = new ObjectMapper();
        String latencyString = mapper.writeValueAsString(latencyMeasurements);
        connection.sendMeasurements(latencyString);
    }

    private LoadSender getSender() {
        latencyMeasurements = new HashMap<>();
        return switch (this.getConnectionType()) {
            case TCP -> new TCPLoadSender(address,port,latencyMeasurements);
            case UDP -> new UDPLoadSender(address,port,latencyMeasurements);
        };
    }


}
