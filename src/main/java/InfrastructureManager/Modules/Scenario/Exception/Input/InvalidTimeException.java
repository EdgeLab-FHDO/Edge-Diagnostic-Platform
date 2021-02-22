package InfrastructureManager.Modules.Scenario.Exception.Input;

/**
 * Signals errors related to time management in Scenarios. Normally deals with invalid times (for example starting times in the past)
 */
public class InvalidTimeException extends ScenarioInputException {
    /**
     * Constructor of the class. Creates a new Invalid time exception.
     *
     * @param message Message to be passed to the exception
     */
    public InvalidTimeException(String message) {
        super(message);
    }
}
