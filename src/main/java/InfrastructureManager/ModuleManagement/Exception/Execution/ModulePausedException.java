package InfrastructureManager.ModuleManagement.Exception.Execution;

import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleExecutionException;

public class ModulePausedException extends ModuleExecutionException {
    public ModulePausedException(String message) {
        super(message);
    }
}
