package DiagnosticsClient.Load.TCP;

import DiagnosticsClient.Load.ClientSocketOptions;
import com.fasterxml.jackson.annotation.JsonGetter;

public class TCPClientSocketOptions extends ClientSocketOptions {
    private boolean keepAlive;
    private boolean nagleAlgorithm;
    private int linger;


    public TCPClientSocketOptions() {
        //Default values from the java doc and local run test
        super();
        this.keepAlive = false;
        this.nagleAlgorithm=true;
        this.linger=-1;
    }

    @JsonGetter("keepAlive")
    public boolean getKeepAlive() {
        return keepAlive;
    }

    public void setKeepAlive(boolean keepAlive) {
        this.keepAlive = keepAlive;
    }

    @JsonGetter("nagleAlgorithm")
    public boolean getNagleAlgorithm() {
        return nagleAlgorithm;
    }

    public void setNagleAlgorithm(boolean nagleAlgorithm) {
        this.nagleAlgorithm = nagleAlgorithm;
    }

    public int getLinger() {
        return linger;
    }

    public void setLinger(int linger) {
        this.linger = linger;
    }
}
