package InfrastructureManager;

public class EdgeNode {
    private String id;
    private boolean connected;

    public EdgeNode(String id, boolean connected) {
        this.id = id;
        this.connected = connected;
    }

    public String getId() {
        return id;
    }

    public boolean isConnected() {
        return connected;
    }
}
