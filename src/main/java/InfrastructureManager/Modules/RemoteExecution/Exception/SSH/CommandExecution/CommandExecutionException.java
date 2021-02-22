package InfrastructureManager.Modules.RemoteExecution.Exception.SSH.CommandExecution;

import InfrastructureManager.Modules.RemoteExecution.Exception.SSH.SSHException;

/**
 * Signals errors occurred while trying to execute a command remotely using a {@link InfrastructureManager.Modules.RemoteExecution.Output.SSHClient}
 */
public class CommandExecutionException extends SSHException {

    /**
     * Constructor of the class. Creates a new Command execution exception.
     *
     * @param message the message to be passed to the exception
     * @param cause   the cause of the exception (Normally another exception)
     */
    public CommandExecutionException(String message, Throwable cause) {
        super(message, cause);
    }
}
