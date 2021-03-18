package DiagnosticsClient;

import DiagnosticsClient.Communication.ClientPlatformConnection;
import DiagnosticsClient.Communication.Exception.ClientCommunicationException;
import DiagnosticsClient.Communication.ServerInformation;
import DiagnosticsClient.Load.*;
import DiagnosticsClient.Load.Exception.LoadSendingException;
import DiagnosticsClient.Load.Exception.TCP.ServerNotSetUpException;
import DiagnosticsClient.Load.LoadTypes.DiagnosticsLoad;
import DiagnosticsClient.Load.LoadTypes.FileLoad;
import DiagnosticsClient.Load.LoadTypes.PingLoad;
import DiagnosticsClient.Load.TCP.TCPClientSocketOptions;
import DiagnosticsClient.Load.UDP.UDPClientSocketOptions;
import REST.Exception.RESTClientException;
import LoadManagement.BasicLoadManager.*;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.File;
import java.io.IOException;

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

    public void sendLoad(ConnectionType connection, DiagnosticsLoad load) throws LoadSendingException {
        this.loadManager.setConnectionType(connection);
        this.loadManager.sendLoad(load);
    }

    public void setSocketOptions(ClientSocketOptions options) {
        this.loadManager.setSocketOptions(options);
    }

    public static void main(String[] args) {
        try {
            if (args.length > 0) {
                String baseURL = args[0];
                String registerURL = args[1];
                String assignURL = args[2];
                String getServerURL = args[3];
                Client activeClient = new Client(baseURL,registerURL,assignURL,getServerURL);
                PingLoad ping = new PingLoad(1000,1,4);
                FileLoad file = new FileLoad(10,10,8 * 1024L);
                //FileLoad file = new FileLoad("src/main/resources/test2.txt");
                TCPClientSocketOptions options = new TCPClientSocketOptions(); // Default
                //UDPClientSocketOptions options = new UDPClientSocketOptions(); //Default
                activeClient.setSocketOptions(options);
                //activeClient.sendLoad(ConnectionType.TCP,ping);
                activeClient.sendLoad(ConnectionType.TCP,file);
            } else {
                System.out.println("No arguments");
            }
        } catch (ClientCommunicationException | LoadSendingException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
