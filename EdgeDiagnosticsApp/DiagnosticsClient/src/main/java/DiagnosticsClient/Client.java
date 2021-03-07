package DiagnosticsClient;

import DiagnosticsClient.Communication.ClientPlatformConnection;
import DiagnosticsClient.Communication.Exception.ClientCommunicationException;
import DiagnosticsClient.Communication.ServerInformation;
import DiagnosticsClient.Load.ClientLoadManager;
import DiagnosticsClient.Load.Exception.LoadSendingException;
import DiagnosticsClient.Load.Exception.TCP.ServerNotSetUpException;
import REST.Exception.RESTClientException;
import LoadManagement.BasicLoadManager.*;
import com.fasterxml.jackson.core.JsonProcessingException;

public class Client {

    private final ClientLoadManager loadManager;

    public Client(String baseURL, String registerURL, String assignURL, String getServerURL) throws ClientCommunicationException {
        try {
            ClientPlatformConnection connection = new ClientPlatformConnection(baseURL, registerURL, assignURL, getServerURL);
            connection.register(this.getJsonRepresentation());
            ServerInformation server = connection.getServer(this.getJsonRepresentation());
            this.loadManager = new ClientLoadManager(server);
        } catch (JsonProcessingException | RESTClientException | ServerNotSetUpException e) {
            throw new ClientCommunicationException("Communication with diagnostics platform failed: ", e);
        }
    }

    private String getJsonRepresentation() {
        String id = "diagnostics_client";
        return  "{\"id\":\"" + id + "\"}";
    }

    public void sendLoad(ConnectionType connection, LoadType load) throws LoadSendingException {
        this.loadManager.setConnectionType(connection);
        this.loadManager.setLoadType(load);
        this.loadManager.sendLoad();
    }

    public static void main(String[] args) {
        try {
            if (args.length > 0) {
                String baseURL = args[0];
                String registerURL = args[1];
                String assignURL = args[2];
                String getServerURL = args[3];
                Client activeClient = new Client(baseURL,registerURL,assignURL,getServerURL);
                activeClient.sendLoad(ConnectionType.TCP,LoadType.PING);
            } else {
                System.out.println("No arguments");
            }
        } catch (ClientCommunicationException | LoadSendingException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
