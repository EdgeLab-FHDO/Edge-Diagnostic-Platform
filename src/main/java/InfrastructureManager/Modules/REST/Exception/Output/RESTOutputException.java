package InfrastructureManager.Modules.REST.Exception.Output;

import InfrastructureManager.Modules.REST.Exception.RESTModuleException;

/**
 * General exception type that represent errors while using a {@link InfrastructureManager.Modules.REST.Output.GETOutput} output.
 */
public class RESTOutputException extends RESTModuleException {
    /**
     * Constructor of the class. Creates a new Rest output exception.
     *
     * @param message The message to be passed to the exception.
     */
    public RESTOutputException(String message) {
        super(message);
    }

}
