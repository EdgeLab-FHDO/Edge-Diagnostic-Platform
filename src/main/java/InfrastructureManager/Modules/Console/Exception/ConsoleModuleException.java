package InfrastructureManager.Modules.Console.Exception;

import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleExecutionException;

public class ConsoleModuleException extends ModuleExecutionException {
    public ConsoleModuleException(String message) {
        super(message);
    }
}
