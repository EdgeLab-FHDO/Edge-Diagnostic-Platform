package InfrastructureManager.Configuration.Exception;

/**
 * Signals that for a given command, the response was not defined in the configuration file
 */
public class ResponseNotDefinedException extends CommandSetException {

    /**
     * @param message Message to be set into the exception.
     */
    public ResponseNotDefinedException(String message) {
        super(message);
    }
}
