package InfrastructureManager.ModuleManagement.Exception.Creation;

import InfrastructureManager.ModuleManagement.Exception.Creation.ModuleCreationException;

public class ModuleNotFoundException extends ModuleCreationException {
    public ModuleNotFoundException(String message) {
        super(message);
    }
}
