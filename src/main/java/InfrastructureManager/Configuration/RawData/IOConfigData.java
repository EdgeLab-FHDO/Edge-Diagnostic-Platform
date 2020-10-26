package InfrastructureManager.Configuration.RawData;

public class IOConfigData {

    private String type;
    private String name;
    private int port;

    public IOConfigData() {
        this.type = null;
        this.name = null;
        this.port = 0;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public int getPort() {
        return port;
    }
}
