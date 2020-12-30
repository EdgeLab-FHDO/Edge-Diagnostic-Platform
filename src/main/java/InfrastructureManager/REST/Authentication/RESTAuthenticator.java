package InfrastructureManager.Rest.Authentication;

/**
 * Interface to create different authenticators for REST requests
 */
public interface RESTAuthenticator {
    boolean authenticate();
}
