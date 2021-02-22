package InfrastructureManager.Modules.RemoteExecution.Exception.SSH;

/**
 * Signals that an {@link InfrastructureManager.Modules.RemoteExecution.Output.SSHClient} is trying to be used
 * without being configured and initialized first.
 *
 * Initializing includes setting up username, password, SSH port and host name
 */
public class ClientNotInitializedException extends SSHException {
    /**
     * Constructor of the class. Creates a new Client not initialized exception.
     *
     * @param message the message to be passed to the exception
     */
    public ClientNotInitializedException(String message) {
        super(message);
    }
}
