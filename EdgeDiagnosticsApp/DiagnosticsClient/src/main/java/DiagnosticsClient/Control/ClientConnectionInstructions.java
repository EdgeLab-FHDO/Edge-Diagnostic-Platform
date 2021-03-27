package DiagnosticsClient.Control;

import Control.Instruction.ConnectionInstructions;
import DiagnosticsClient.Load.ClientSocketOptions;
import LoadManagement.BasicLoadManager.ConnectionType;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ClientConnectionInstructions extends ConnectionInstructions {
    private final ClientSocketOptions socketOptions;

    public ClientConnectionInstructions(@JsonProperty("type") ConnectionType type,
                                        @JsonProperty("socketOptions") ClientSocketOptions socketOptions) {
        super(type);
        this.socketOptions = socketOptions;
    }

    public ClientSocketOptions getSocketOptions() {
        return socketOptions;
    }
}
