package DiagnosticsServer.Load;

public class ServerSocketOptions {
    private int receiveBufferSize;
    private boolean reuseAddress;
    private int timeout;

    public ServerSocketOptions() {
        this.receiveBufferSize = 65536;
        this.timeout = 0;
        this.reuseAddress = false;
    }

    public int getReceiveBufferSize() {
        return receiveBufferSize;
    }

    public void setReceiveBufferSize(int receiveBufferSize) {
        this.receiveBufferSize = receiveBufferSize;
    }

    public boolean getReuseAddress() {
        return reuseAddress;
    }

    public void setReuseAddress(boolean reuseAddress) {
        this.reuseAddress = reuseAddress;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}
