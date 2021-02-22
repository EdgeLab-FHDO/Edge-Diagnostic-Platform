package InfrastructureManager.Modules.AdvantEDGE.Exception;

import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleExecutionException;


/**
 * Signals different errors in execution of the {@link InfrastructureManager.Modules.AdvantEDGE.AdvantEdgeModule}
 */
public class AdvantEdgeModuleException extends ModuleExecutionException {
    /**
     * Constructor of the class. Creates a new exception based only on a message
     * @param message Message to be passed to the exception.
     */
    public AdvantEdgeModuleException(String message) {
        super(message);
    }

    /**
     * Constructor of the class. Creates a new exception based on a message and a cause
     * @param message Message to be passed to the exception
     * @param cause {@link Throwable} that caused the exception (normally another exception)
     */
    public AdvantEdgeModuleException(String message, Throwable cause) {
        super(message, cause);
    }

}
