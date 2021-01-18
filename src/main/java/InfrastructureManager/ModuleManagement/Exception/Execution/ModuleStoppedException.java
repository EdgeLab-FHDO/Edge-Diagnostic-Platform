package InfrastructureManager.ModuleManagement.Exception.Execution;

import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleExecutionException;

public class ModuleStoppedException extends ModuleExecutionException {
    public ModuleStoppedException() {
        super("Module was stopped");
    }
}
