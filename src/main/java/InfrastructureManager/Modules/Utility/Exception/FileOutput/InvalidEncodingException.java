package InfrastructureManager.Modules.Utility.Exception.FileOutput;

/**
 * Signals that a specified encoding for an output file is not supported by the platform
 */
public class InvalidEncodingException extends FileOutputException {
    /**
     * Constructor of the class. Creates a new exception.
     *
     * @param message the message to be passed into the exception
     * @param cause   the cause of the exception (Normally another exception)
     */
    public InvalidEncodingException(String message, Throwable cause) {
        super(message, cause);
    }
}
