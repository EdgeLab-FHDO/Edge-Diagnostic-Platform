package InfrastructureManager.ModuleManagement.Exception.Creation;

import InfrastructureManager.ModuleManagement.Exception.Creation.ModuleCreationException;

public class IncorrectIONameException extends ModuleCreationException {
    public IncorrectIONameException(String message) {
        super(message);
    }
}
