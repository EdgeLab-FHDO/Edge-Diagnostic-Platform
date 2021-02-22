package InfrastructureManager.Modules.REST.Exception;

import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleExecutionException;

/**
 * General exception type that represents errors while using a {@link InfrastructureManager.Modules.REST.RESTModule}
 */
public class RESTModuleException extends ModuleExecutionException {
    /**
     * Constructor of the class. Creates a new Rest module exception.
     *
     * @param message The message to be passed to the exception.
     */
    public RESTModuleException(String message) {
        super(message);
    }

}
