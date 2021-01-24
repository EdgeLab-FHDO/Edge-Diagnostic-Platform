package InfrastructureManager.Modules.Utility.Exception.MasterController;

import InfrastructureManager.Modules.Utility.Exception.UtilityModuleException;

public class ModuleControllerException extends UtilityModuleException {
    public ModuleControllerException(String message) {
        super(message);
    }

    public ModuleControllerException(String message, Throwable cause) {
        super(message, cause);
    }
}
