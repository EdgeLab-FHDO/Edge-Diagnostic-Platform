package DiagnosticsClient.Load.Exception.FileLoad;

import DiagnosticsClient.Load.Exception.LoadSendingException;

public class FileLoadException extends LoadSendingException {
    public FileLoadException(String message) {
        super(message);
    }

    public FileLoadException(String message, Throwable cause) {
        super(message, cause);
    }
}
