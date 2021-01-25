package InfrastructureManager.Configuration.Exception;

/**
 * Signals errors in the configuration process
 */
public class ConfigurationException extends Exception {

    /**
     * @param message Message to be set into the exception
     * @param cause Throwable that caused the exception
     */
    public ConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}
