package DiagnosticsClient.Load.LoadTypes;

import DiagnosticsClient.Load.Exception.FileLoad.FileCreationException;
import DiagnosticsClient.Load.Exception.FileLoad.FileLoadException;
import LoadManagement.LoadType;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class FileLoad extends RepetitiveLoad {

    private final File fileToSend;

    public FileLoad(int times, int waitBetweenSend_ms,long sizeInBytes) throws FileCreationException {
        super(LoadType.FILE,times,waitBetweenSend_ms,sizeInBytes);
        String path = "src/main/resources/fileLoad";
        this.fileToSend = createFileBySize(path, sizeInBytes);
    }

    public FileLoad(int times, int waitBetweenSend_ms,String filePath) throws FileLoadException {
        super(LoadType.FILE,times,waitBetweenSend_ms,0);
        File temp = new File(filePath);
        if (!temp.exists()) throw new FileLoadException("File does not exist!");
        this.fileToSend = temp;
        this.setDataLength(temp.length());
    }

    public File getFileToSend() {
        return fileToSend;
    }


    private File createFileBySize(String path, long sizeInBytes) throws FileCreationException {
        File result = new File(path);
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
