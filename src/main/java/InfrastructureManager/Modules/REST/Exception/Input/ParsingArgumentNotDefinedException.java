package InfrastructureManager.Modules.REST.Exception.Input;

/**
 * Signals that when using a {@link InfrastructureManager.Modules.REST.Input.POSTInput}, an argument is trying to
 * be parsed from the body of the request which was not defined initially in the module configuration.
 */
public class ParsingArgumentNotDefinedException extends RESTInputException {
    /**
     * Constructor of the class. Creates a new exception.
     *
     * @param message The message to be passed to the exception.
     */
    public ParsingArgumentNotDefinedException(String message) {
        super(message);
    }
}
