package DiagnosticsClient.Load.UDP;

import DiagnosticsClient.Load.ClientSocketOptions;

public class UDPClientSocketOptions extends ClientSocketOptions {

    private boolean broadcast;
    private boolean multicastLoop;
    private int multicastTTL;


    public UDPClientSocketOptions() {
        super();
        this.broadcast = true;
        this.multicastTTL = 1;
        this.multicastLoop = true;
    }

    public boolean getBroadcast() {
        return broadcast;
    }

    public void setBroadcast(boolean broadcast) {
        this.broadcast = broadcast;
    }

    public boolean getMulticastLoop() {
        return multicastLoop;
    }

    public void setMulticastLoop(boolean multicastLoop) {
        this.multicastLoop = multicastLoop;
    }

    public int getMulticastTTL() {
        return multicastTTL;
    }

    public void setMulticastTTL(int multicastTTL) {
        this.multicastTTL = multicastTTL;
    }
}
