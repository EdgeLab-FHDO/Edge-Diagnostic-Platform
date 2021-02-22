package InfrastructureManager.Modules.Scenario.Exception.Input;

import InfrastructureManager.Modules.Scenario.Exception.ScenarioModuleException;

/**
 * General exception type that represents error in a {@link InfrastructureManager.Modules.Scenario.Scenario} object.
 */
public class ScenarioInputException extends ScenarioModuleException {
    /**
     * Constructor of the class. Creates a new Scenario input exception.
     *
     * @param message Message to be passed to the exception.
     */
    public ScenarioInputException(String message) {
        super(message);
    }
}
