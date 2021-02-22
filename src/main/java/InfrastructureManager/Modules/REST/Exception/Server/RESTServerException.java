package InfrastructureManager.Modules.REST.Exception.Server;

import InfrastructureManager.Modules.REST.Exception.RESTModuleException;

/**
 * Signals errors in the built-in REST server
 */
public class RESTServerException extends RESTModuleException {
    /**
     * Constructor of the class. Creates a new Rest server exception.
     *
     * @param message The message to be passed to the exception.
     */
    public RESTServerException(String message) {
        super(message);
    }
}
