package DiagnosticsServer.Load;

import DiagnosticsServer.Load.Exception.LoadReceivingException;
import LoadManagement.BasicLoadManager;

public class ServerLoadManager extends BasicLoadManager {

    private final int port;

    public ServerLoadManager(int port) {
        this.port = port;
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
