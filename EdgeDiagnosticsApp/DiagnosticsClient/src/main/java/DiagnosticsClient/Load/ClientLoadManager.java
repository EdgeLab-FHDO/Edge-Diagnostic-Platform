package DiagnosticsClient.Load;

import DiagnosticsClient.Load.Exception.LoadSendingException;
import DiagnosticsClient.Load.Exception.TCP.ServerNotSetUpException;
import DiagnosticsClient.Communication.ServerInformation;
import DiagnosticsClient.Load.LoadTypes.DiagnosticsLoad;
import DiagnosticsClient.Load.TCP.TCPLoadSender;
import DiagnosticsClient.Load.UDP.UDPLoadSender;
import LoadManagement.BasicLoadManager;

public class ClientLoadManager extends BasicLoadManager {

    private final String address;
    private final int port;
    private ClientSocketOptions options;

    public ClientLoadManager(ServerInformation serverInformation) throws ServerNotSetUpException {
        if (serverInformation == null) throw new ServerNotSetUpException("Client is not connected to the server");
        this.address = serverInformation.getIpAddress();
        this.port = serverInformation.getPort();
    }

    public void setSocketOptions(ClientSocketOptions options) {
        this.options = options;
    }

    public void sendLoad(DiagnosticsLoad load) throws LoadSendingException {
        LoadSender sender = getSender();
        if (this.options != null) sender.changeSocketConfiguration(this.options);
        sender.send(load);
    }

    private LoadSender getSender() {
        return switch (this.getConnectionType()) {
            case TCP -> new TCPLoadSender(address,port);
            case UDP -> new UDPLoadSender(address,port);
        };
    }
}
