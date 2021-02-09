package InfrastructureManager.ModuleManagement.Exception.Creation;

/**
 * Signals that a certain {@link InfrastructureManager.ModuleManagement.PlatformModule} could not be found
 * in the defined modules. This maybe due to errors in the naming or lack of a defined {@link InfrastructureManager.ModuleManagement.ModuleFactory.ModuleType}
 */
public class ModuleNotDefinedException extends ModuleCreationException {
    public ModuleNotDefinedException(String message) {
        super(message);
    }
}
