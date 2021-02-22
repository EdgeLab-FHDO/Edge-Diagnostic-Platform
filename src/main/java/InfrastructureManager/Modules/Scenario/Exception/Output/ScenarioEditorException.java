package InfrastructureManager.Modules.Scenario.Exception.Output;

import InfrastructureManager.Modules.Scenario.Exception.ScenarioModuleException;

/**
 * Signals error while operating a {@link InfrastructureManager.Modules.Scenario.ScenarioEditor}
 */
public class ScenarioEditorException extends ScenarioModuleException {
    /**
     * Constructor of the class. Creates a new Scenario editor exception.
     *
     * @param message Message to be passed to the exception
     */
    public ScenarioEditorException(String message) {
        super(message);
    }

    /**
     * Constructor of the class. Creates a new Scenario editor exception based on a message and a cause.
     *
     * @param message Message to be passed to the exception
     * @param cause   The cause of the exception (Normally another exception).
     */
    public ScenarioEditorException(String message, Throwable cause) {
        super(message, cause);
    }
}
