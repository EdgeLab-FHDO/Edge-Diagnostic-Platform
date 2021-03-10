package DiagnosticsClient.Load;

public class ClientSocketOptions {
    private int receiveBufferSize;
    private int sendBufferSize;
    private boolean reuseAddress;
    private int ipTOS;
    private int timeout;

    public ClientSocketOptions() {
        //Default values from the java doc and local run test
        this.reuseAddress=false;
        this.ipTOS=0;
        this.receiveBufferSize=65536;
        this.sendBufferSize=65536;
        this.timeout = 0;
    }

    public boolean getReuseAddress() {
        return reuseAddress;
    }

    public void setReuseAddress(boolean reuseAddress) {
        this.reuseAddress = reuseAddress;
    }

    public int getIpTOS() {
        return ipTOS;
    }

    public void setIpTOS(int ipTOS) {
        this.ipTOS = ipTOS;
    }

    public int getReceiveBufferSize() {
        return receiveBufferSize;
    }

    public void setReceiveBufferSize(int receiveBufferSize) {
        this.receiveBufferSize = receiveBufferSize;
    }

    public int getSendBufferSize() {
        return sendBufferSize;
    }

    public void setSendBufferSize(int sendBufferSize) {
        this.sendBufferSize = sendBufferSize;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}
