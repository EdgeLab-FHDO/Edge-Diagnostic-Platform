package InfrastructureManager.ModuleManagement.Exception.Creation;

import InfrastructureManager.ModuleManagement.Exception.Creation.ModuleCreationException;

public class IncorrectInputException extends ModuleCreationException {
    public IncorrectInputException(String message) {
        super(message);
    }
}
