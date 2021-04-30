package DiagnosticsClient.Load.Exception;

public class LoadSendingException  extends Exception{
    public LoadSendingException(String message) {
        super(message);
    }

    public LoadSendingException(String message, Throwable cause) {
        super(message, cause);
    }
}
