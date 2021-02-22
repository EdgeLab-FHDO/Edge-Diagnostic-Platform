package InfrastructureManager.Modules.RemoteExecution.Exception.SSH.FileSending;

/**
 * Signals errors that come as a result of the SCP protocol used to send files remotely using a {@link InfrastructureManager.Modules.RemoteExecution.Output.SSHClient}
 */
public class SCPProtocolException extends FileSendingException {
    /**
     * Constructor of the class. Creates a new Scp protocol exception.
     *
     * @param message the message to be passed to the exception
     */
    public SCPProtocolException(String message) {
        super(message);
    }
}
