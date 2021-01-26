package InfrastructureManager.Modules.Utility.Exception;

import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleExecutionException;

public class UtilityModuleException extends ModuleExecutionException {
    public UtilityModuleException(String message) {
        super(message);
    }

    public UtilityModuleException(String message, Throwable cause) {
        super(message, cause);
    }
}
