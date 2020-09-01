package InfrastructureManager.Rest.Utility.Authentication;

public class SampleAuthentication implements MasterAuthentication {
    @Override
    public boolean authenticate() {
        return true;
    }
}
