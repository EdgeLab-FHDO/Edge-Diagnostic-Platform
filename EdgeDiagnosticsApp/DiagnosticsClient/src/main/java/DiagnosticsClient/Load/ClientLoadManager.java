package DiagnosticsClient.Load;

import DiagnosticsClient.Load.Exception.LoadSendingException;
import DiagnosticsClient.Load.Exception.TCP.ServerNotSetUpException;
import DiagnosticsClient.Communication.ServerInformation;
import LoadManagement.BasicLoadManager;

public class ClientLoadManager extends BasicLoadManager {

    private final String address;
    private final int port;

    public ClientLoadManager(ServerInformation serverInformation) throws ServerNotSetUpException {
        if (serverInformation == null) throw new ServerNotSetUpException("Client is not connected to the server");
        this.address = serverInformation.getIpAddress();
        this.port = serverInformation.getPort();
    }

    public void sendLoad(DiagnosticsLoad load) throws LoadSendingException {
        LoadSender sender = getSender();
        sender.send(load);
    }

    private LoadSender getSender() {
        return switch (this.getConnectionType()) {
            case TCP -> new TCPLoadSender(address,port);
            case UDP -> new UDPLoadSender(address,port);
        };
    }
}
