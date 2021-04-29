package DiagnosticsServer.Control.BufferControl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BufferInformation {
    private final int inputStreamBuffer;
    private final int receiveBuffer;

    @JsonCreator
    public BufferInformation(@JsonProperty("inputStreamBuffer") int inputStreamBuffer,
                             @JsonProperty("receiveBuffer") int receiveBuffer) {
        this.inputStreamBuffer = inputStreamBuffer;
        this.receiveBuffer = receiveBuffer;
    }

    public int getInputStreamBuffer() {
        return inputStreamBuffer;
    }

    public int getReceiveBuffer() {
        return receiveBuffer;
    }
}
