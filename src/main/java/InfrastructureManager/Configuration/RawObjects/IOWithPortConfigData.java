package InfrastructureManager.Configuration.RawObjects;

public class IOWithPortConfigData extends IOConfigData {

    private int port;

    public IOWithPortConfigData() {
        super();
        this.port = 0;
    }

    public int getPort() {
        return port;
    }
}
