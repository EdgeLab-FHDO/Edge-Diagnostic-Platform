package InfrastructureManager.Configuration.Exception;


import InfrastructureManager.Configuration.CommandSet;

/**
 * Symbols errors in the configuration or use of the {@link CommandSet}
 */
public class CommandSetException extends Exception {

    /**
     * @param message Message to be set into the exception
     */
    public CommandSetException(String message) {
        super(message);
    }
}
