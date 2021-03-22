package DiagnosticsClient.Control.RawData;

import DiagnosticsClient.Load.ClientSocketOptions;
import DiagnosticsClient.Load.LoadTypes.DiagnosticsLoad;
import LoadManagement.BasicLoadManager;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ClientInstruction {
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

    public BasicLoadManager.ConnectionType getConnectionType() {
        return  connection.getType();
    }
}
