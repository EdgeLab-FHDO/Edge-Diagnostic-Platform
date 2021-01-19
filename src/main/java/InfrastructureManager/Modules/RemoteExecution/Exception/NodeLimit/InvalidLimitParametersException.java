package InfrastructureManager.Modules.RemoteExecution.Exception.NodeLimit;

public class InvalidLimitParametersException extends NodeLimitException {
    public InvalidLimitParametersException(String message) {
        super(message);
    }

    public InvalidLimitParametersException(String message, Throwable cause) {
        super(message, cause);
    }
}
