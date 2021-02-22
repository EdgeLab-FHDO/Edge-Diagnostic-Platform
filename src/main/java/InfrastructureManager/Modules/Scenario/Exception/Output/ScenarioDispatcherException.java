package InfrastructureManager.Modules.Scenario.Exception.Output;

import InfrastructureManager.Modules.Scenario.Exception.ScenarioModuleException;

/**
 * Signals errors while operating a {@link InfrastructureManager.Modules.Scenario.ScenarioDispatcher}
 */
public class ScenarioDispatcherException extends ScenarioModuleException {
    /**
     * Constructor of the class. Creates a new Scenario dispatcher exception.
     *
     * @param message Message to be passed to the exception.
     */
    public ScenarioDispatcherException(String message) {
        super(message);
    }
}
