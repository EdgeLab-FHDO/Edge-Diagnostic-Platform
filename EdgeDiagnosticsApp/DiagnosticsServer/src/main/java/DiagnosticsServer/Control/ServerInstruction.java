package DiagnosticsServer.Control;

import Control.Instruction.Instruction;
import DiagnosticsServer.Load.ServerSocketOptions;
import LoadManagement.BasicLoadManager;
import LoadManagement.LoadType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ServerInstruction implements Instruction {
    private final LoadType loadType;
    private final ServerConnectionInstructions connection;

    @JsonCreator
    public ServerInstruction(@JsonProperty("loadType") LoadType loadType,
                             @JsonProperty("connection") ServerConnectionInstructions connection) {
        this.loadType = loadType;
        this.connection = connection;
    }

    @Override
    public LoadType getLoadType() {
        return loadType;
    }

    public ServerSocketOptions getSocketOptions() {
        return this.connection.getSocketOptions();
    }

    @Override
    public BasicLoadManager.ConnectionType getConnectionType() {
        return this.connection.getType();
    }
}
