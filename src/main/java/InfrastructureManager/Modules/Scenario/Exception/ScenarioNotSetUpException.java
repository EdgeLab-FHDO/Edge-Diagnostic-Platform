package InfrastructureManager.Modules.Scenario.Exception;

/**
 * Signals that in a {@link InfrastructureManager.Modules.Scenario.ScenarioModule}, the {@link InfrastructureManager.Modules.Scenario.Scenario}
 * has not been set up properly and is trying to be accessed
 */
public class ScenarioNotSetUpException extends ScenarioModuleException {
    /**
     * Constructor of the class. Creates a new Scenario not set up exception.
     *
     * @param message Message to be passed to the exception.
     */
    public ScenarioNotSetUpException(String message) {
        super(message);
    }
}
