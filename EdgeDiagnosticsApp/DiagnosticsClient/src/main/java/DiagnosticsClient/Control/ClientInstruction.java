package DiagnosticsClient.Control;

import DiagnosticsClient.Load.ClientSocketOptions;
import DiagnosticsClient.Load.LoadTypes.DiagnosticsLoad;
import LoadManagement.BasicLoadManager.ConnectionType;
import LoadManagement.LoadType;
import Control.Instruction.Instruction;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ClientInstruction implements Instruction {
    private final DiagnosticsLoad load;
    private final ClientConnectionInstructions connection;

    @JsonCreator
    public ClientInstruction(@JsonProperty("load") DiagnosticsLoad load,
                             @JsonProperty("connection") ClientConnectionInstructions connection) {
        this.load = load;
        this.connection = connection;
    }

    public ClientConnectionInstructions getConnection() {
        return connection;
    }

    public DiagnosticsLoad getLoad() {
        return this.load;
    }

    public ClientSocketOptions getSocketOptions() {
        return connection.getSocketOptions();
    }

    @Override
    public LoadType getLoadType() {
        return this.load.getType();
    }

    @Override
    public ConnectionType getConnectionType() {
        return  connection.getType();
    }
}
