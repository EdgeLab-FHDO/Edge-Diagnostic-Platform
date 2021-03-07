package LoadManagement;

public abstract class BasicLoadManager {
    public enum LoadType {PING,FILE,VIDEO}
    public enum ConnectionType {TCP,UDP}

    private ConnectionType connectionType;
    private LoadType loadType;

    public ConnectionType getConnectionType() {
        return connectionType;
    }

    public LoadType getLoadType() {
        return loadType;
    }

    public void setConnectionType(ConnectionType connectionType) {
        this.connectionType = connectionType;
    }

    public void setLoadType(LoadType loadType) {
        this.loadType = loadType;
    }
}
