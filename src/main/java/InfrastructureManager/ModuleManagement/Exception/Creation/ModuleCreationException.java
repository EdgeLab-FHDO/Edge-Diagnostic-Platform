package InfrastructureManager.ModuleManagement.Exception.Creation;

/**
 * Signals an error while creating or configuring the instances of {@link InfrastructureManager.ModuleManagement.PlatformModule}
 */
public class ModuleCreationException extends Exception {

    /**
     * Constructor of the class.
     * @param message Message to pass to the exception
     */
    public ModuleCreationException(String message) {
        super(message);
    }
}
