package InfrastructureManager.Modules.RemoteExecution.Exception.SSH;

import InfrastructureManager.Modules.RemoteExecution.Exception.RemoteExecutionModuleException;

public class SSHException extends RemoteExecutionModuleException {
    public SSHException(String message) {
        super(message);
    }

    public SSHException(String message, Throwable cause) {
        super(message, cause);
    }
}
