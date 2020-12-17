package InfrastructureManager.NewREST.Authentication;

public class DummyAuthentication implements RESTAuthenticator {
    @Override
    public boolean authenticate() {
        return true;
    }
}
