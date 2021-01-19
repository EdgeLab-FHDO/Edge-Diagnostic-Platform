package InfrastructureManager.Modules.REST.Exception.Input;

import InfrastructureManager.Modules.REST.Exception.RESTModuleException;

public class RESTInputException extends RESTModuleException {
    public RESTInputException(String message) {
        super(message);
    }

    public RESTInputException(String message, Throwable cause) {
        super(message, cause);
    }
}
