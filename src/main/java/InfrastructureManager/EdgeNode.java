package InfrastructureManager;

public class EdgeNode {
    private String id;
    private String ipAddress;
    private boolean connected;
    private long resource;

    public EdgeNode() {
        this.id = null;
        this.ipAddress = null;
        this.connected = false;
        this.resource = 0;
    }

    public EdgeNode(String id, String ip, boolean connected, long thisResource) {
        this.id = id;
        this.ipAddress = ip;
        this.connected = connected;
        this.resource = thisResource;
    }

    public String getId() {
        return id;
    }

    public boolean isConnected() {
        return connected;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public long getResource() { return resource;}

    //TODO: edit this to accommodate new data parameter. Or just use pretty print tbh :))
    @Override
    public String toString() {
        return "EdgeNode{" +
                "id='" + id + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                ", connected=" + connected + '\'' +
                ", resource=" + resource +
                '}';
    }
}
