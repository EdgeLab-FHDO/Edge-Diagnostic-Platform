package InfrastructureManager.Modules.REST.Authentication;

/**
 * Implementation of RESTAuthenticator interface to server as a placeholder until real authentication is
 * implemented.
 */
public class DummyAuthentication implements RESTAuthenticator {
    /**
     * Dummy authenticate implementation
     * @return true always
     */
    @Override
    public boolean authenticate() {
        return true;
    }
}
