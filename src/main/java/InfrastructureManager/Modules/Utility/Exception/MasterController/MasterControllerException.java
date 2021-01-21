package InfrastructureManager.Modules.Utility.Exception.MasterController;

import InfrastructureManager.Modules.Utility.Exception.UtilityModuleException;

public class MasterControllerException extends UtilityModuleException {
    public MasterControllerException(String message) {
        super(message);
    }

    public MasterControllerException(String message, Throwable cause) {
        super(message, cause);
    }
}
