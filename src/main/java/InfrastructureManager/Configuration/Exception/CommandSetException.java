package InfrastructureManager.Configuration.Exception;


import InfrastructureManager.Configuration.CommandSet;

/**
 * Exception Type to model error in the configuration or use of the {@link CommandSet}
 */
public class CommandSetException extends Exception {
    public CommandSetException(String message) {
        super(message);
    }
}
