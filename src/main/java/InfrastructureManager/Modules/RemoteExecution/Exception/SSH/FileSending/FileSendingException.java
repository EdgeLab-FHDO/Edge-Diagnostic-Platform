package InfrastructureManager.Modules.RemoteExecution.Exception.SSH.FileSending;

import InfrastructureManager.Modules.RemoteExecution.Exception.SSH.SSHException;

public class FileSendingException extends SSHException {
    public FileSendingException(String message) {
        super(message);
    }

    public FileSendingException(String message, Throwable cause) {
        super(message, cause);
    }
}
