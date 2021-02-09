package InfrastructureManager.ModuleManagement.Exception.Execution;

/**
 * Signal errors in run-time while executing {@link InfrastructureManager.ModuleManagement.PlatformModule}
 * instances.
 */
public class ModuleExecutionException extends Exception{
    /**
     * Constructor of the class
     * @param message Message to be passed to the exception
     */
    public ModuleExecutionException(String message) {
        super(message);
    }

    /**
     * Additional constructor of the class
     * @param message Message to be passed to the exception
     * @param cause Cause of the exception (Throwable)
     */
    public ModuleExecutionException(String message, Throwable cause) {
        super(message, cause);
    }
}
