package DiagnosticsClient.Control;

import DiagnosticsClient.Control.BufferInformation;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class FileBufferInformation extends BufferInformation {
    private final int fileReadingBuffer;
    private final int fileInputBuffer;

    @JsonCreator
    public FileBufferInformation(@JsonProperty("outputStreamBuffer") int outputStreamBuffer,
                                 @JsonProperty("inputReaderBuffer") int inputReaderBuffer,
                                 @JsonProperty("fileReadingBuffer") int fileReadingBuffer,
                                 @JsonProperty("fileInputBuffer") int fileInputBuffer) {
        super(outputStreamBuffer, inputReaderBuffer);
        this.fileReadingBuffer = fileReadingBuffer;
        this.fileInputBuffer = fileInputBuffer;
    }

    public int getFileReadingBuffer() {
        return fileReadingBuffer;
    }

    public int getFileInputBuffer() {
        return fileInputBuffer;
    }
}
