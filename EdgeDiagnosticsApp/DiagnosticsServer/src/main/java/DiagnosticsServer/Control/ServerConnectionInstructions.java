package DiagnosticsServer.Control;

import Control.Instruction.ConnectionInstructions;
import DiagnosticsServer.Control.BufferControl.BufferInformation;
import DiagnosticsServer.Load.ServerSocketOptions;
import LoadManagement.BasicLoadManager.ConnectionType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ServerConnectionInstructions extends ConnectionInstructions {

    private final ServerSocketOptions socketOptions;
    private final BufferInformation bufferInformation;

    @JsonCreator
    public ServerConnectionInstructions(@JsonProperty("type") ConnectionType type,
                                        @JsonProperty("socketOptions") ServerSocketOptions socketOptions,
                                        @JsonProperty("bufferInformation") BufferInformation bufferInformation
    ) {
        super(type);
        this.socketOptions = socketOptions;
        this.bufferInformation = bufferInformation;
    }

    public ServerSocketOptions getSocketOptions() {
        return socketOptions;
    }

    public BufferInformation getBufferInformation() {
        return bufferInformation;
    }
}
