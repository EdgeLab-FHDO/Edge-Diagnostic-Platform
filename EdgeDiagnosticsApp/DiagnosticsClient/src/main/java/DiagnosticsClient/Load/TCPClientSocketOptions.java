package DiagnosticsClient.Load;

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

    public boolean getKeepAlive() {
        return keepAlive;
    }

    public void setKeepAlive(boolean keepAlive) {
        this.keepAlive = keepAlive;
    }

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
