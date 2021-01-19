package InfrastructureManager.Modules.RemoteExecution.Exception.SSH;

public class ClientNotInitializedException extends SSHException {
    public ClientNotInitializedException(String message) {
        super(message);
    }
}
