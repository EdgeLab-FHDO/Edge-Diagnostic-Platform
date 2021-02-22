package InfrastructureManager.Modules.RemoteExecution.Exception;

import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleExecutionException;

/**
 * General exception class that signals error while using a {@link InfrastructureManager.Modules.RemoteExecution.RemoteExecutionModule}
 */
public class RemoteExecutionModuleException extends ModuleExecutionException {
    /**
     * Constructor of the class. Creates a new Remote execution module exception.
     *
     * @param message the message to be passed to the exception
     */
    public RemoteExecutionModuleException(String message) {
        super(message);
    }

    /**
     * Constructor of the class. Creates a new Remote execution module exception.
     *
     * @param message the message to be passed to the exception
     * @param cause   the cause of the exception (Normally another exception)
     */
    public RemoteExecutionModuleException(String message, Throwable cause) {
        super(message, cause);
    }
}
