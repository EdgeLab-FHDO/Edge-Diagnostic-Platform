package InfrastructureManager.ModuleManagement.Exception.Creation;

/**
 * Signals that, in configuring a module, an incorrect name for an input or output was declared. This
 * normally means that the structure of {@code <MODULE_NAME>.<IO_NAME>} was not followed.
 */
public class IncorrectIONameException extends ModuleCreationException {
    public IncorrectIONameException(String message) {
        super(message);
    }
}
