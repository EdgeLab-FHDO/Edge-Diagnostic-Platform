package Communication.Exception;

public class RESTClientException extends Exception {
    public RESTClientException(String message) {
        super(message);
    }

    public RESTClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
