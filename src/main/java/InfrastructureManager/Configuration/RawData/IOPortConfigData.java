package InfrastructureManager.Configuration.RawData;

public class IOPortConfigData{

    private String name;
    private int port;

    public IOPortConfigData() {
        this.name = null;
        this.port = 0;
    }

    public String getName() {
        return name;
    }

    public int getPort() {
        return port;
    }
}
