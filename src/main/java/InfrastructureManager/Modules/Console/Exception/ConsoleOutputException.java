package InfrastructureManager.Modules.Console.Exception;

/**
 * Signals an error in the console output
 */
public class ConsoleOutputException extends ConsoleModuleException {

    /**
     * Constructor of the class
     * @param message Messaged to be passed into the exception.
     */
    public ConsoleOutputException(String message) {
        super(message);
    }
}
