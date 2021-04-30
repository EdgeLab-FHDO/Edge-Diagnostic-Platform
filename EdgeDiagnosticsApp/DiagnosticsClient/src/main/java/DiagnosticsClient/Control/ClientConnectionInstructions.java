package DiagnosticsClient.Control;

import Control.Instruction.ConnectionInstructions;
import DiagnosticsClient.Load.ClientSocketOptions;
import LoadManagement.BasicLoadManager.ConnectionType;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ClientConnectionInstructions extends ConnectionInstructions {
    private final ClientSocketOptions socketOptions;
    private final BufferInformation bufferInformation;

    public ClientConnectionInstructions(@JsonProperty("type") ConnectionType type,
                                        @JsonProperty("socketOptions") ClientSocketOptions socketOptions,
                                        @JsonProperty("bufferInformation") BufferInformation bufferInformation
    ) {
        super(type);
        this.socketOptions = socketOptions;
        this.bufferInformation = bufferInformation;
    }

    public ClientSocketOptions getSocketOptions() {
        return socketOptions;
    }

    public BufferInformation getBufferInformation() {
        return bufferInformation;
    }
}
