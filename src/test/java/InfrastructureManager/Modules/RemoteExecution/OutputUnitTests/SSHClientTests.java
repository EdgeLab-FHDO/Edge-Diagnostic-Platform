package InfrastructureManager.Modules.RemoteExecution.OutputUnitTests;

import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleExecutionException;
import InfrastructureManager.Modules.CommonTestingMethods;
import InfrastructureManager.Modules.RemoteExecution.Exception.SSH.ClientNotInitializedException;
import InfrastructureManager.Modules.RemoteExecution.Exception.SSH.FileSending.InvalidFileException;
import InfrastructureManager.Modules.RemoteExecution.Exception.SSH.SSHException;
import InfrastructureManager.Modules.RemoteExecution.Output.SSHClient;
import org.junit.Test;

public class SSHClientTests {

    private final SSHClient client = new SSHClient("test_ssh");

    private void assertExceptionInClient(Class<? extends  Throwable> exceptionClass, String expectedMessage, String commandToClient) {
        CommonTestingMethods.assertException(exceptionClass,expectedMessage,() -> client.write(commandToClient));
    }

    @Test
    public void invalidCommandThrowsException() {
        String command = "ssh notACommand";
        String expected = "Invalid command notACommand for SSHClient";
        assertExceptionInClient(SSHException.class, expected, command);
    }

    @Test
    public void incompleteCommandThrowsException() {
        String command = "ssh setup";
        String expected = "Arguments missing for command " + command + " to SSHClient";
        assertExceptionInClient(SSHException.class, expected, command);
    }

    @Test
    public void executeCommandNeedsToHaveDisplayFlag() {
        String command = "ssh execute ls";
        String expected = "Wrong Display parameter in SSHClient execute command";
        assertExceptionInClient(SSHException.class, expected, command);
    }

    @Test
    public void clientHasToBeSetUpBeforeExecuting(){
        String command = "ssh execute -f ls";
        String expected = "SSH Client has not been set up";
        assertExceptionInClient(ClientNotInitializedException.class, expected, command);
    }

    @Test
    public void clientHasToBeSetUpBeforeSendingFiles(){
        String command = "ssh sendFile file1 /home/file2";
        String expected = "SSH Client has not been set up";
        assertExceptionInClient(ClientNotInitializedException.class, expected, command);
    }

    @Test
    public void invalidFileThrowsException() throws ModuleExecutionException {
        client.write("ssh setup 192.168.0.1 22 username pass");
        String command = "ssh sendFile /file1 /home/file2";
        String expected = "Invalid File in /file1";
        assertExceptionInClient(InvalidFileException.class, expected, command);
    }
}
