package InfrastructureManager.Modules.RemoteExecution.Exception.SSH.FileSending;

import InfrastructureManager.Modules.RemoteExecution.Exception.SSH.SSHException;

/**
 * Signals errors while sending files to a remote machine using a {@link InfrastructureManager.Modules.RemoteExecution.Output.SSHClient}
 */
public class FileSendingException extends SSHException {
    /**
     * Constructor of the class. Creates a new File sending exception.
     *
     * @param message the message to be passed to the exception
     */
    public FileSendingException(String message) {
        super(message);
    }

    /**
     * Constructor of the class. Creates a new File sending exception.
     *
     * @param message the message to be passed to the exception
     * @param cause   the cause of the exception (Normally another exception)
     */
    public FileSendingException(String message, Throwable cause) {
        super(message, cause);
    }
}
