package InfrastructureManager.Modules.Scenario.Exception.Output;

/**
 * Signals errors while reading/writing {@link InfrastructureManager.Modules.Scenario.Scenario} objects from/to files.
 */
public class ScenarioIOException extends ScenarioEditorException {
    /**
     * Constructor of the class. Creates a new ScenarioIOException.
     *
     * @param message Message to be passed to the exception
     * @param cause   The cause of the exception (Normally another exception).
     */
    public ScenarioIOException(String message, Throwable cause) {
        super(message, cause);
    }
}
