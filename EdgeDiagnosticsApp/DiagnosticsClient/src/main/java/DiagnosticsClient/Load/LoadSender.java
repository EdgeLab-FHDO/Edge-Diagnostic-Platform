package DiagnosticsClient.Load;

import DiagnosticsClient.Load.Exception.LoadSendingException;

public abstract class LoadSender {

    private final String address;
    private final int port;

    public LoadSender(String address, int port) {
        this.address = address;
        this.port = port;
    }

    public String getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    public abstract void send(DiagnosticsLoad load) throws LoadSendingException;
}
