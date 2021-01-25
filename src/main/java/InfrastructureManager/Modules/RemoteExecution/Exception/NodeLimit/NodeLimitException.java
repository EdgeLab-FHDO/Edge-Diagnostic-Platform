package InfrastructureManager.Modules.RemoteExecution.Exception.NodeLimit;

import InfrastructureManager.Modules.RemoteExecution.Exception.RemoteExecutionModuleException;

public class NodeLimitException extends RemoteExecutionModuleException {
    public NodeLimitException(String message) {
        super(message);
    }

    public NodeLimitException(String message, Throwable cause) {
        super(message, cause);
    }
}
