package DiagnosticsClient.Load.LoadTypes;

import DiagnosticsClient.Load.Exception.FileLoad.FileCreationException;
import DiagnosticsClient.Load.Exception.FileLoad.FileLoadException;
import LoadManagement.LoadType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class FileLoad extends RepetitiveLoad {

    @JsonIgnore
    private final File fileToSend;

    @JsonCreator
    public FileLoad(@JsonProperty("times") int times,
                    @JsonProperty("interval") int waitBetweenSend_ms,
                    @JsonProperty("dataLength") long sizeInBytes,
                    @JsonProperty("src") String src) throws FileLoadException {
        super(LoadType.FILE,times,waitBetweenSend_ms,sizeInBytes);
        if (src == null || src.equals("")) {
            String path = "src/main/resources/fileLoad";
            this.fileToSend = createFileBySize(path, sizeInBytes);
        } else {
            File temp = new File(src);
            if (!temp.exists()) throw new FileLoadException("File does not exist!");
            this.fileToSend = temp;
            this.setDataLength(temp.length());
        }
    }


    public File getFileToSend() {
        return fileToSend;
    }


    private File createFileBySize(String path, long sizeInBytes) throws FileCreationException {
        File result = new File(path);
        if (result.exists()) result.delete();
        try {
            if (result.createNewFile()) {
                RandomAccessFile auxFile = new RandomAccessFile(result,"rw");
                auxFile.setLength(sizeInBytes);
                auxFile.close();
                return result;
            } else {
                throw new FileCreationException("File could not be created");
            }
        } catch (IOException e) {
            throw new FileCreationException("File could not be created", e);
        }
    }
}
