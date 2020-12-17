package InfrastructureManager.REST.Authentication;

public class DummyAuthentication implements RESTAuthenticator {
    @Override
    public boolean authenticate() {
        return true;
    }
}
