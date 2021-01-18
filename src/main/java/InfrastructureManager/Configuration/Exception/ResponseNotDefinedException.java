package InfrastructureManager.Configuration.Exception;

/**
 * Signals that for a given command, the response was not defined in the configuration file
 */
public class ResponseNotDefinedException extends CommandSetException {

    public ResponseNotDefinedException(String message) {
        super(message);
    }
}
