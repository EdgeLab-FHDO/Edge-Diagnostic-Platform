package InfrastructureManager.Configuration.Exception;

/**
 * Signals errors in the configuration process
 */
public class ConfigurationException extends Exception {

    public ConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}
