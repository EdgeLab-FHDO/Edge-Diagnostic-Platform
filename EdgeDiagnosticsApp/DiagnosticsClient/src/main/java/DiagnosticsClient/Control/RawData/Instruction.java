package DiagnosticsClient.Control.RawData;

import DiagnosticsClient.Load.ClientSocketOptions;
import DiagnosticsClient.Load.LoadTypes.DiagnosticsLoad;
import LoadManagement.BasicLoadManager;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Instruction {
    private final DiagnosticsLoad load;
    private final ConnectionInstructions connection;

    @JsonCreator
    public Instruction(@JsonProperty("load") DiagnosticsLoad load,
                       @JsonProperty("connection") ConnectionInstructions connection) {
        this.load = load;
        this.connection = connection;
    }

    public ConnectionInstructions getConnection() {
        return connection;
    }

    public DiagnosticsLoad getLoad() {
        return this.load;
    }

    public ClientSocketOptions getSocketOptions() {
        return connection.getSocketOptions();
    }

    public BasicLoadManager.ConnectionType getConnectionType() {
        return  connection.getType();
    }
}
