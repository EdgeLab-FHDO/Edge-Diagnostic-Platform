package InfrastructureManager.Modules.Scenario.Exception.Output;

import InfrastructureManager.Modules.Scenario.Exception.ScenarioModuleException;

public class ScenarioEditorException extends ScenarioModuleException {
    public ScenarioEditorException(String message) {
        super(message);
    }

    public ScenarioEditorException(String message, Throwable cause) {
        super(message, cause);
    }
}
