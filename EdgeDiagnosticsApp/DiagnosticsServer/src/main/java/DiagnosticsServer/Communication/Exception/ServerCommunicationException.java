package DiagnosticsServer.Communication.Exception;

public class ServerCommunicationException extends Exception{
    public ServerCommunicationException(String message) {
        super(message);
    }

    public ServerCommunicationException(String message, Throwable cause) {
        super(message, cause);
    }
}
