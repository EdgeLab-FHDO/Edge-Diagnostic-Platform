package InfrastructureManager.Modules.RemoteExecution.Exception;

import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleExecutionException;

public class RemoteExecutionModuleException extends ModuleExecutionException {
    public RemoteExecutionModuleException(String message) {
        super(message);
    }

    public RemoteExecutionModuleException(String message, Throwable cause) {
        super(message, cause);
    }
}
