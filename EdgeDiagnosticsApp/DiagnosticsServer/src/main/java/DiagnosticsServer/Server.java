package DiagnosticsServer;

import DiagnosticsServer.Communication.Exception.ServerCommunicationException;
import DiagnosticsServer.Communication.RawServerData;
import DiagnosticsServer.Communication.ServerPlatformConnection;
import DiagnosticsServer.Load.Exception.LoadReceivingException;
import DiagnosticsServer.Load.ServerLoadManager;
import DiagnosticsServer.Load.ServerSocketOptions;
import DiagnosticsServer.Load.UDP.UDPServerSocketOptions;
import LoadManagement.BasicLoadManager.ConnectionType;
import LoadManagement.LoadType;
import REST.Exception.RESTClientException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Server {

    private final ServerLoadManager loadManager;
    private final int port;

    public Server(int port, String baseURL, String registerURL) throws ServerCommunicationException {
        try {
            this.port = port;
            this.loadManager = new ServerLoadManager(this.port);
            ServerPlatformConnection connection = new ServerPlatformConnection(baseURL, registerURL);
            connection.register(this.getJsonRepresentation());
        } catch (RESTClientException | UnknownHostException | JsonProcessingException e) {
            throw new ServerCommunicationException("Communication with Diagnostics Platform failed: ", e);
        }
    }

    private String getIP() throws UnknownHostException {
        InetAddress myIP = InetAddress.getLocalHost();
        return myIP.getHostAddress();
    }

    private String getJsonRepresentation() throws UnknownHostException, JsonProcessingException {
        String id = "diagnostics_server";
        String completeAddress = this.getIP() + ":" + this.port;
        RawServerData rawServerData = new RawServerData(id, completeAddress);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(rawServerData);
    }

    public void receiveLoad(ConnectionType connection, LoadType load) throws LoadReceivingException {
        this.loadManager.setConnectionType(connection);
        this.loadManager.setLoadType(load);
        this.loadManager.receiveLoad();
    }

    public void setSocketOptions(ServerSocketOptions options) {
        this.loadManager.setSocketOptions(options);
    }

    public static void main(String[] args) {
        try {
            if (args.length > 0) {
                String baseURL = args[0];
                String registerURL = args[1];
                Server activeInstance = new Server(4445, baseURL,registerURL);
                ServerSocketOptions options = new ServerSocketOptions();
                //UDPServerSocketOptions options = new UDPServerSocketOptions();
                activeInstance.setSocketOptions(options);
                System.out.println("Starting Server");
                activeInstance.receiveLoad(ConnectionType.TCP, LoadType.FILE);
            } else {
                System.out.println("No arguments");
            }
        } catch (ServerCommunicationException | LoadReceivingException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
