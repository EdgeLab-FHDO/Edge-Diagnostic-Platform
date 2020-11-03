package InfrastructureManager;

public class EdgeNode {
    private String id;
    private String ipAddress;
    private boolean connected;
    private int port;

    public EdgeNode() {
        this.id = null;
        this.ipAddress = null;
        this.connected = false;
        this.port = 0;
    }

    public EdgeNode(String id, String ip, boolean connected, int port) {
        this.id = id;
        this.ipAddress = ip;
        this.connected = connected;
        this.port = port;
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

    public int getPort() {
        return port;
    }

    @Override
    public String toString() {
        return "EdgeNode{" +
                "id='" + id + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                ", connected=" + connected + '\'' +
                ", port=" + port +
                '}';
    }
}
