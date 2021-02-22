package InfrastructureManager.Modules.Scenario.Exception.Input;

/**
 * Signals that in a {@link InfrastructureManager.Modules.Scenario.Scenario}, the owner module has not been set up.
 */
public class OwnerModuleNotSetUpException extends ScenarioInputException {
    /**
     * Constructor of the class. Creates a new OwnerModuleNotSetUp exception.
     *
     * @param message Message to be passed to the exception.
     */
    public OwnerModuleNotSetUpException(String message) {
        super(message);
    }
}
