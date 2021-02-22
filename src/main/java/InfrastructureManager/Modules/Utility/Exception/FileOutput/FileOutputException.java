package InfrastructureManager.Modules.Utility.Exception.FileOutput;

import InfrastructureManager.Modules.Utility.Exception.UtilityModuleException;
import InfrastructureManager.Modules.Utility.Output.FileOutput;

/**
 * Signals errors in a {@link FileOutput} output of a Utility module
 */
public class FileOutputException extends UtilityModuleException {
    /**
     * Constructor of the class. Creates a new exception.
     *
     * @param message the message to be passed to the exception
     */
    public FileOutputException(String message) {
        super(message);
    }

    /**
     * Constructor of the class. Creates a new exception.
     *
     * @param message the message to be passed to the exception
     * @param cause   the cause of the exception (Normally another exception)
     */
    public FileOutputException(String message, Throwable cause) {
        super(message, cause);
    }
}
