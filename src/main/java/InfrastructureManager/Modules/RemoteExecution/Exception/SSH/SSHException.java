package InfrastructureManager.Modules.RemoteExecution.Exception.SSH;

import InfrastructureManager.Modules.RemoteExecution.Exception.RemoteExecutionModuleException;

/**
 * Signals errors while using an output of type {@link InfrastructureManager.Modules.RemoteExecution.Output.SSHClient}
 */
public class SSHException extends RemoteExecutionModuleException {
    /**
     * Constructor of the class. Creates a new Ssh exception.
     *
     * @param message the message to be passed to the exception
     */
    public SSHException(String message) {
        super(message);
    }

    /**
     * Constructor of the class. Creates a new Ssh exception.
     *
     * @param message the message to be passed to the exception
     * @param cause   the cause of the exception (Normally another exception)
     */
    public SSHException(String message, Throwable cause) {
        super(message, cause);
    }
}
