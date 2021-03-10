package DiagnosticsServer.Load;

import DiagnosticsServer.Load.Exception.LoadReceivingException;
import LoadManagement.LoadType;

public abstract class LoadReceiver {

    private final int port;

    public LoadReceiver(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public abstract void receive(LoadType loadType) throws LoadReceivingException;
    public abstract void changeSocketConfiguration(ServerSocketOptions options);
}
