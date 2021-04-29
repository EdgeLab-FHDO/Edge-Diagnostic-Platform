package LoadManagement;

public abstract class BasicLoadManager {
    public enum ConnectionType {TCP,UDP}

    private ConnectionType connectionType;

    public ConnectionType getConnectionType() {
        return connectionType;
    }

    public void setConnectionType(ConnectionType connectionType) {
        this.connectionType = connectionType;
    }

}
