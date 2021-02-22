package InfrastructureManager.Modules.RemoteExecution.Exception.NodeLimit;

import InfrastructureManager.Modules.RemoteExecution.Exception.RemoteExecutionModuleException;

/**
 * Signals errors in creating or sending resource limits to nodes
 */
public class NodeLimitException extends RemoteExecutionModuleException {
    /**
     * Constructor of the class. Creates a new Node limit exception.
     *
     * @param message the message to be passed to the exception
     */
    public NodeLimitException(String message) {
        super(message);
    }

    /**
     * Constructor of the class. Creates a new Node limit exception.
     *
     * @param message the message to be passed to the exception
     * @param cause   the cause of the exception (Normally another exception)
     */
    public NodeLimitException(String message, Throwable cause) {
        super(message, cause);
    }
}
