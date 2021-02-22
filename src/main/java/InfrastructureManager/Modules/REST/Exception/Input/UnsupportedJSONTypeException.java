package InfrastructureManager.Modules.REST.Exception.Input;

/**
 * Signals that when using a {@link InfrastructureManager.Modules.REST.Input.POSTInput}, an argument is trying to
 * be parse from the request body which has a json type not supported by the platform.
 *
 * Not supported types are Array, JSON object and null.
 */
public class UnsupportedJSONTypeException extends RESTInputException {
    /**
     * Constructor of the class. Creates a new exception.
     *
     * @param message The message to be passed to the exception.
     */
    public UnsupportedJSONTypeException(String message) {
        super(message);
    }
}
