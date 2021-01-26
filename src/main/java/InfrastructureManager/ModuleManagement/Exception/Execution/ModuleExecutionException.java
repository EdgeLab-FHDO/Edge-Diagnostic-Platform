package InfrastructureManager.ModuleManagement.Exception.Execution;

public class ModuleExecutionException extends Exception{
    public ModuleExecutionException(String message) {
        super(message);
    }

    public ModuleExecutionException(String message, Throwable cause) {
        super(message, cause);
    }
}
