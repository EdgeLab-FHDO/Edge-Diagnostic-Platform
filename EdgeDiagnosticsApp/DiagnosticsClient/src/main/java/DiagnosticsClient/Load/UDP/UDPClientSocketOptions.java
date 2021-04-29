package DiagnosticsClient.Load.UDP;

import DiagnosticsClient.Load.ClientSocketOptions;
import com.fasterxml.jackson.annotation.JsonGetter;

public class UDPClientSocketOptions extends ClientSocketOptions {

    private boolean broadcast;

    public UDPClientSocketOptions() {
        super();
        this.broadcast = false;
    }

    @JsonGetter("broadcast")
    public boolean getBroadcast() {
        return broadcast;
    }

    public void setBroadcast(boolean broadcast) {
        this.broadcast = broadcast;
    }
}
