package InfrastructureManager.REST.Authentication;

//todo: Comment and javadoc
public class DummyAuthentication implements RESTAuthenticator {
    @Override
    public boolean authenticate() {
        return true;
    }
}
