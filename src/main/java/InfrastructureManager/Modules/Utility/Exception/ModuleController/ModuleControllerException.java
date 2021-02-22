package InfrastructureManager.Modules.Utility.Exception.ModuleController;

import InfrastructureManager.Modules.Utility.Exception.UtilityModuleException;
import InfrastructureManager.Modules.Utility.Output.ModuleController;

/**
 * Signals errors in the {@link ModuleController} output of a Utility module
 *
 */
public class ModuleControllerException extends UtilityModuleException {
    /**
     * Constructor of the class. Creates a new exception.
     *
     * @param message the message to be passed to the exception
     */
    public ModuleControllerException(String message) {
        super(message);
    }

    /**
     * Constructor of the class. Creates a new exception.
     *
     * @param message the message to be passed to be exception
     * @param cause   the cause of the exception (Normally another exception)
     */
    public ModuleControllerException(String message, Throwable cause) {
        super(message, cause);
    }
}
