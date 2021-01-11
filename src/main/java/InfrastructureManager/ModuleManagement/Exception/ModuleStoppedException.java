package InfrastructureManager.ModuleManagement.Exception;

public class ModuleStoppedException extends ModuleException {
    public ModuleStoppedException() {
        super("Module was stopped");
    }
}
