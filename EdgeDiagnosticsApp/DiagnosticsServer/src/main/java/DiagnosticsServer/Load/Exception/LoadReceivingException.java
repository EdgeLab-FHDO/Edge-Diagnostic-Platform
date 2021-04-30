package DiagnosticsServer.Load.Exception;

public class LoadReceivingException extends Exception {
    public LoadReceivingException(String message) {
        super(message);
    }

    public LoadReceivingException(String message, Throwable cause) {
        super(message, cause);
    }
}
