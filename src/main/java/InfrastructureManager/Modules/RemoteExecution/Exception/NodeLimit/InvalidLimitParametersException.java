package InfrastructureManager.Modules.RemoteExecution.Exception.NodeLimit;

/**
 * Signals that invalid parameters were passed to add a limit into the {@link InfrastructureManager.Modules.RemoteExecution.LimitList}
 *
 * Invalid parameters include for example null parameters, non numeric characters in quota or period or invalid floating point definition (, instead of .)
 */
public class InvalidLimitParametersException extends NodeLimitException {

    /**
     * Constructor of the Class. Creates a new exception
     *
     * @param message the message to be passed to the exception
     * @param cause   the cause of the exception (Normally other exception)
     */
    public InvalidLimitParametersException(String message, Throwable cause) {
        super(message, cause);
    }
}
