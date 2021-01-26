package InfrastructureManager.ModuleManagement.Exception.Execution;

public class ModuleStoppedException extends ModuleExecutionException {
    public ModuleStoppedException() {
        super("Module was stopped");
    }
}
