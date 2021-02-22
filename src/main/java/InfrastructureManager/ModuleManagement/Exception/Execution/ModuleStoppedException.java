package InfrastructureManager.ModuleManagement.Exception.Execution;

/**
 * Signals that the current module was stopped externally.
 */
public class ModuleStoppedException extends ModuleExecutionException {
    /**
     * Constructor of the class. Creates an exception with the message:
     * "Module was stopped"
     */
    public ModuleStoppedException() {
        super("Module was stopped");
    }
}
