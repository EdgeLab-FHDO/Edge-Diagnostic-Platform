package Communication.Exception;

public class ErrorInRequestException extends RESTClientException {
    public ErrorInRequestException(int responseStatus) {
        super("The request failed. Response status:" + responseStatus);
    }
}
