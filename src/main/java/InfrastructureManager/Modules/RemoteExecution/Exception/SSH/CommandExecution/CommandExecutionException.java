package InfrastructureManager.Modules.RemoteExecution.Exception.SSH.CommandExecution;

import InfrastructureManager.Modules.RemoteExecution.Exception.SSH.SSHException;

public class CommandExecutionException extends SSHException {

    public CommandExecutionException(String message, Throwable cause) {
        super(message, cause);
    }
}
