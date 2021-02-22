package InfrastructureManager.Modules.Utility.Exception;

import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleExecutionException;

/**
 * Signals a general error in the execution of  an Utility module.
 * <p>
 * More specific exception exist, to signal errors in the specific outputs like:
 * - {@link InfrastructureManager.Modules.Utility.Exception.FileOutput.FileOutputException}
 * - {@link InfrastructureManager.Modules.Utility.Exception.ModuleController.ModuleControllerException}
 *
 */
public class UtilityModuleException extends ModuleExecutionException {
    /**
     * Constructor of the class. Creates a new exception.
     *
     * @param message the message to be passed to the exception
     */
    public UtilityModuleException(String message) {
        super(message);
    }

    /**
     * Constructor of the class. Creates a new exception.
     *
     * @param message the message to be passed to the exception
     * @param cause   the cause of the exception (Normally another exception)
     */
    public UtilityModuleException(String message, Throwable cause) {
        super(message, cause);
    }
}
