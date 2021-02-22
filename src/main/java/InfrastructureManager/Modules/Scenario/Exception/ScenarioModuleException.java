package InfrastructureManager.Modules.Scenario.Exception;

import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleExecutionException;

/**
 * General exception type to represent errors in the operation of a {@link InfrastructureManager.Modules.Scenario.ScenarioModule}
 */
public class ScenarioModuleException extends ModuleExecutionException {
    /**
     * Constructor of the class. Creates a new Scenario module exception.
     *
     * @param message Message to be passed to the exception
     */
    public ScenarioModuleException(String message) {
        super(message);
    }

    /**
     * Constructor of the class. Creates a new Scenario module exception.
     *
     * @param message Message to be passed to the exception
     * @param cause   The cause of the exception (Normally another exception)
     */
    public ScenarioModuleException(String message, Throwable cause) {
        super(message, cause);
    }
}
