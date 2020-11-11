package InfrastructureManager.Configuration.RawData;

public class IOConfigData {

    private final String type;
    private final String name;
    private final String address;
    private final int port;

    public IOConfigData() {
        this.type = null;
        this.name = null;
        this.address = null;
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

    public String getAddress() {
        return address;
    }
}
