package DiagnosticsClient.Communication.Exception;

public class ClientCommunicationException extends Exception {
    public ClientCommunicationException(String message) {
        super(message);
    }

    public ClientCommunicationException(String message, Throwable cause) {
        super(message, cause);
    }
}
