package InfrastructureManager.Modules.Scenario.Exception;

import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleExecutionException;

public class ScenarioModuleException extends ModuleExecutionException {
    public ScenarioModuleException(String message) {
        super(message);
    }

    public ScenarioModuleException(String message, Throwable cause) {
        super(message, cause);
    }
}
