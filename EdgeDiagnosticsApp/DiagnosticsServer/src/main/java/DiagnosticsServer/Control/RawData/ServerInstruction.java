package DiagnosticsServer.Control.RawData;

import DiagnosticsServer.Load.ServerSocketOptions;
import LoadManagement.BasicLoadManager;
import LoadManagement.LoadType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ServerInstruction {
    private final LoadType loadType;
    private final ServerConnectionInstructions connection;

    @JsonCreator
    public ServerInstruction(@JsonProperty("loadType") LoadType loadType,
                             @JsonProperty("connection") ServerConnectionInstructions connection) {
        this.loadType = loadType;
        this.connection = connection;
    }

    public LoadType getLoadType() {
        return loadType;
    }

    public ServerConnectionInstructions getConnection() {
        return connection;
    }

    public ServerSocketOptions getSocketOptions() {
        return this.connection.getSocketOptions();
    }

    public BasicLoadManager.ConnectionType getConnectionType() {
        return this.connection.getType();
    }
}
