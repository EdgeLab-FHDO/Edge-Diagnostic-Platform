package DiagnosticsServer.Load;

import DiagnosticsServer.Load.Exception.LoadReceivingException;
import DiagnosticsServer.Load.TCP.TCPLoadReceiver;
import DiagnosticsServer.Load.UDP.UDPLoadReceiver;
import LoadManagement.BasicLoadManager;
import LoadManagement.LoadType;

public class ServerLoadManager extends BasicLoadManager {

    private final int port;
    private LoadType loadType;

    public ServerLoadManager(int port) {
        this.port = port;
    }

    public void setLoadType(LoadType loadType) {
        this.loadType = loadType;
    }

    public LoadType getLoadType() {
        return loadType;
    }

    public void receiveLoad() throws LoadReceivingException {
        LoadReceiver receiver = getReceiver();
        switch (this.getLoadType()) {
            case PING -> receiver.receivePing(port);
            case FILE -> receiver.receiveFile(port);
            case VIDEO -> receiver.receiveVideo(port);
        }
    }

    private LoadReceiver getReceiver() {
        return switch (this.getConnectionType()) {
            case TCP -> new TCPLoadReceiver();
            case UDP -> new UDPLoadReceiver();
        };
    }
}
