package InfrastructureManager.Modules.Utility.Exception.FileOutput;

import InfrastructureManager.Modules.Utility.Exception.UtilityModuleException;

public class FileOutputException extends UtilityModuleException {
    public FileOutputException(String message) {
        super(message);
    }

    public FileOutputException(String message, Throwable cause) {
        super(message, cause);
    }
}
