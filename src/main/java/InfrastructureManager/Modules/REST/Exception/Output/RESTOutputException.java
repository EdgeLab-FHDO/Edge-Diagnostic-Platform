package InfrastructureManager.Modules.REST.Exception.Output;

import InfrastructureManager.Modules.REST.Exception.RESTModuleException;

public class RESTOutputException extends RESTModuleException {
    public RESTOutputException(String message) {
        super(message);
    }

    public RESTOutputException(String message, Throwable cause) {
        super(message, cause);
    }
}
