package DiagnosticsServer.Control.RawData;

import Control.ConnectionInstructions;
import DiagnosticsServer.Load.ServerSocketOptions;
import LoadManagement.BasicLoadManager.ConnectionType;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ServerConnectionInstructions extends ConnectionInstructions {

    private final ServerSocketOptions socketOptions;

    public ServerConnectionInstructions(@JsonProperty("type") ConnectionType type,
                                        @JsonProperty("socketOptions") ServerSocketOptions socketOptions) {
        super(type);
        this.socketOptions = socketOptions;
    }

    public ServerSocketOptions getSocketOptions() {
        return socketOptions;
    }
}
