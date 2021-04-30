package DiagnosticsClient.Load.Exception.FileLoad;

public class FileCreationException extends FileLoadException {
    public FileCreationException(String message) {
        super(message);
    }

    public FileCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
