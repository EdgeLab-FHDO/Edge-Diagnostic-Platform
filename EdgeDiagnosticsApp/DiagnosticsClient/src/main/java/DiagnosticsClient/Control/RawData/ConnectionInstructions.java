package DiagnosticsClient.Control.RawData;

import DiagnosticsClient.Load.ClientSocketOptions;
import LoadManagement.BasicLoadManager.ConnectionType;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ConnectionInstructions {
    private final ConnectionType type;
    private final ClientSocketOptions socketOptions;

    public ConnectionInstructions(@JsonProperty("type") ConnectionType type,
                                  @JsonProperty("socketOptions") ClientSocketOptions socketOptions) {
        this.type = type;
        this.socketOptions = socketOptions;
    }

    public ConnectionType getType() {
        return type;
    }

    public ClientSocketOptions getSocketOptions() {
        return socketOptions;
    }
}
