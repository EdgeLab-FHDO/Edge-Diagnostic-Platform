package InfrastructureManager.Modules.Console.Exception;

import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleExecutionException;

/**
 * Signals an error in the console module. Is a general execution type, and more specific ones,
 * like {@link ConsoleOutputException} should be thrown.
 */
public class ConsoleModuleException extends ModuleExecutionException {
    /**
     * Constructor of the class
     * @param message Message to be passed in the exception.
     */
    public ConsoleModuleException(String message) {
        super(message);
    }
}
