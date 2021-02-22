package InfrastructureManager.ModuleManagement.Exception.Execution;

/**
 * Signals that a {@link InfrastructureManager.ModuleManagement.PlatformModule} which is paused is trying to be
 * accessed.
 */
public class ModulePausedException extends ModuleExecutionException {
    public ModulePausedException(String message) {
        super(message);
    }
}
