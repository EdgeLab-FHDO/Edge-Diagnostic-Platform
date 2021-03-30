package DiagnosticsClient.Load;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION, defaultImpl = BufferInformation.class)
@JsonSubTypes({@JsonSubTypes.Type(FileBufferInformation.class)})
public class BufferInformation {
    private final int outputStreamBuffer;
    private final int inputReaderBuffer;

    @JsonCreator
    public BufferInformation(@JsonProperty("outputStreamBuffer") int outputStreamBuffer,
                             @JsonProperty("inputReaderBuffer") int inputReaderBuffer) {
        this.outputStreamBuffer = outputStreamBuffer;
        this.inputReaderBuffer = inputReaderBuffer;
    }

    public int getOutputStreamBuffer() {
        return outputStreamBuffer;
    }

    public int getInputReaderBuffer() {
        return inputReaderBuffer;
    }
}
