package InfrastructureManager.Modules.NetworkStructure.Exception;

import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleExecutionException;

public class NetworkModuleException extends ModuleExecutionException {
    public NetworkModuleException(String errorMessage){
        super(errorMessage);
    }

    public NetworkModuleException(String message, Throwable cause) {
        super(message, cause);
    }
}
