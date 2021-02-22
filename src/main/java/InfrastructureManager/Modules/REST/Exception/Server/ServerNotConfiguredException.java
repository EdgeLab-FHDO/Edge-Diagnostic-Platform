package InfrastructureManager.Modules.REST.Exception.Server;

import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;

/**
 * Signals that the REST server is trying to be accessed before being configured.
 *
 * Is normally thrown if the {@link InfrastructureManager.Modules.REST.RestServerRunner#configure(ImmutablePlatformModule, String, int)} method is not called
 * or another method that uses the server is called before it.
 */
public class ServerNotConfiguredException extends RESTServerException {
    /**
     * Constructor of the class. Creates a new exception.
     *
     * @param message The message to be passed to the exception.
     */
    public ServerNotConfiguredException(String message) {
        super(message);
    }
}
