package InfrastructureManager.Modules.RemoteExecution.Exception.SSH.FileSending;

/**
 * Signals errors with the file to be sent, while trying to send it to a remote machine using a {@link InfrastructureManager.Modules.RemoteExecution.Output.SSHClient}
 */
public class InvalidFileException extends FileSendingException {
    /**
     * Constructor of the class. Creates a new Invalid file exception.
     *
     * @param message the message to be passed to the exception
     */
    public InvalidFileException(String message) {
        super(message);
    }
}
