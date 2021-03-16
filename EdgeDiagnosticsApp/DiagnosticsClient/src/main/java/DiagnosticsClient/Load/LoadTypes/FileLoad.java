package DiagnosticsClient.Load.LoadTypes;

import DiagnosticsClient.Load.Exception.FileLoad.FileCreationException;
import DiagnosticsClient.Load.Exception.FileLoad.FileLoadException;
import LoadManagement.LoadType;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class FileLoad extends DiagnosticsLoad {

    private final File fileToSend;
    private final int times;
    private final int waitBetweenSend_ms;

    public FileLoad(int times, int waitBetweenSend_ms,long sizeInBytes) throws FileCreationException {
        super(LoadType.FILE);
        this.times = times;
        this.waitBetweenSend_ms = waitBetweenSend_ms;
        String path = "src/main/resources/fileLoad";
        this.fileToSend = createFileBySize(path, sizeInBytes);
    }

    public FileLoad(int times, int waitBetweenSend_ms,String filePath) throws FileLoadException {
        super(LoadType.FILE);
        this.times = times;
        this.waitBetweenSend_ms = waitBetweenSend_ms;
        File temp = new File(filePath);
        if (!temp.exists()) throw new FileLoadException("File does not exist!");
        this.fileToSend = temp;
    }

    public File getFileToSend() {
        return fileToSend;
    }

    public int getTimes() {
        return times;
    }

    public int getWaitBetweenSend_ms() {
        return waitBetweenSend_ms;
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
