package InfrastructureManager.ModuleManagement.Exception.Creation;

/**
 * Signals an error in the {@link InfrastructureManager.ModuleManagement.ModuleManager} operation. Normally
 * means the manager instance is used without initializing it first.
 */
public class ModuleManagerException extends ModuleCreationException {
    public ModuleManagerException(String message) {
        super(message);
    }
}
