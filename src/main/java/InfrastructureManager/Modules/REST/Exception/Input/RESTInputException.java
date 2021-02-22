package InfrastructureManager.Modules.REST.Exception.Input;

import InfrastructureManager.Modules.REST.Exception.RESTModuleException;

/**
 * General exception type that represents errors when using a {@link InfrastructureManager.Modules.REST.Input.POSTInput} input.
 */
public class RESTInputException extends RESTModuleException {
    /**
     * Constructor of the class. Creates a new Rest input exception.
     *
     * @param message The message to be passed to the exception.
     */
    public RESTInputException(String message) {
        super(message);
    }

}
