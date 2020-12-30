package Application.Utilities;

public class RemoteExecutionException extends RuntimeException {
    public RemoteExecutionException(Throwable cause) {
        super("Error getting information from Remote",cause);
    }
}
