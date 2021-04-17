package DiagnosticsServer.Load;

import Communication.Exception.RESTClientException;
import DiagnosticsServer.Communication.ServerPlatformConnection;
import DiagnosticsServer.Control.BufferControl.BufferInformation;
import DiagnosticsServer.Load.Exception.LoadReceivingException;
import DiagnosticsServer.Load.TCP.TCPLoadReceiver;
import DiagnosticsServer.Load.UDP.UDPLoadReceiver;
import LoadManagement.BasicLoadManager;
import LoadManagement.LoadType;

public class ServerLoadManager extends BasicLoadManager {

    private final int port;
    private final ServerPlatformConnection connection;
    private LoadType loadType;
    private ServerSocketOptions options;

    public ServerLoadManager(ServerPlatformConnection connection, int port) {
        this.port = port;
        this.connection = connection;
    }

    public void setLoadType(LoadType loadType) {
        this.loadType = loadType;
    }

    public LoadType getLoadType() {
        return loadType;
    }

    public void setSocketOptions(ServerSocketOptions options) {
        this.options = options;
    }

    public void receiveLoad(BufferInformation bufferInformation) throws LoadReceivingException {
        LoadReceiver receiver = getReceiver();
        if (this.options != null) receiver.changeSocketConfiguration(options);
        receiver.receive(this.getLoadType(), bufferInformation);
    }

    private LoadReceiver getReceiver() {
        return switch (this.getConnectionType()) {
            case TCP -> new TCPLoadReceiver(port);
            case UDP -> new UDPLoadReceiver(port);
        };
    }
}
