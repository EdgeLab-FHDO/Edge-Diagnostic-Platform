package InfrastructureManager.Modules.REST.Exception;

import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleExecutionException;

public class RESTModuleException extends ModuleExecutionException {
    public RESTModuleException(String message) {
        super(message);
    }

}