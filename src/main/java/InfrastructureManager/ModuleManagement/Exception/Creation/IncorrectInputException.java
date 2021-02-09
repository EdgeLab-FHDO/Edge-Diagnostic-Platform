package InfrastructureManager.ModuleManagement.Exception.Creation;

/**
 * Signals the use of a {@link InfrastructureManager.ModuleManagement.PlatformInput} that does not
 * correspond to the predefined input for a module.
 */
public class IncorrectInputException extends ModuleCreationException {
    public IncorrectInputException(String message) {
        super(message);
    }
}
