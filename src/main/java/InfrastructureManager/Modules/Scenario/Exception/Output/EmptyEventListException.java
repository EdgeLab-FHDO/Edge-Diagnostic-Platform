package InfrastructureManager.Modules.Scenario.Exception.Output;

/**
 * Signals that a {@link InfrastructureManager.Modules.Scenario.Scenario} that is trying to be run, has no events to run
 */
public class EmptyEventListException extends ScenarioEditorException {
    /**
     * Constructor of the class. Creates a new Empty event list exception.
     *
     * @param message Message to be passed to the exception.
     */
    public EmptyEventListException(String message) {
        super(message);
    }
}
