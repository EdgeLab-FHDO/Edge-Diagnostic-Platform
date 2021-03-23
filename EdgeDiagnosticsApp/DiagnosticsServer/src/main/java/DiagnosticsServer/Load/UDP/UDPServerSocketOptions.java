package DiagnosticsServer.Load.UDP;

import DiagnosticsServer.Load.ServerSocketOptions;

public class UDPServerSocketOptions extends ServerSocketOptions {
    private boolean broadcast;
    private int ipTOS;
    private int sendBufferSize;

    public UDPServerSocketOptions() {
        super();
        this.broadcast = false;
        this.ipTOS=0;
        this.sendBufferSize=65536;
    }

    public boolean getBroadcast() {
        return broadcast;
    }

    public void setBroadcast(boolean broadcast) {
        this.broadcast = broadcast;
    }

    public int getIpTOS() {
        return ipTOS;
    }

    public void setIpTOS(int ipTOS) {
        this.ipTOS = ipTOS;
    }

    public int getSendBufferSize() {
        return sendBufferSize;
    }

    public void setSendBufferSize(int sendBufferSize) {
        this.sendBufferSize = sendBufferSize;
    }
}
