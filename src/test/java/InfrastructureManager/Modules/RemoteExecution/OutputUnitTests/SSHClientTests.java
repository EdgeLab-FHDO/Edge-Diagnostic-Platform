package InfrastructureManager.Modules.RemoteExecution.OutputUnitTests;

import InfrastructureManager.Modules.RemoteExecution.Output.SSHClient;
import org.junit.Assert;
import org.junit.Test;

public class SSHClientTests {

    private final SSHClient client = new SSHClient("test_ssh");

    public void assertException(Class<? extends  Throwable> exceptionClass, String command ,String expectedMessage) {
        var e = Assert.assertThrows(exceptionClass, () -> client.out(command));
        Assert.assertEquals(expectedMessage, e.getMessage());
    }

    @Test
    public void invalidCommandThrowsException() {
        assertException(IllegalArgumentException.class, "ssh notACommand", "Invalid command for SSHClient");
    }

    @Test
    public void incompleteCommandThrowsException() {
        assertException(IllegalArgumentException.class, "ssh setup", "Arguments missing for command - SSHClient");
    }

    @Test
    public void executeCommandNeedsToHaveDisplayFlag() {
        assertException(IllegalArgumentException.class, "ssh execute ls", "Wrong Display parameter in SSHClient execute command");
    }

    @Test
    public void clientHasToBeSetUpBeforeExecuting(){
        assertException(IllegalStateException.class, "ssh execute -f ls", "SSH Client has not been set up");
    }

    @Test
    public void clientHasToBeSetUpBeforeSendingFiles(){
        assertException(IllegalStateException.class, "ssh sendFile file1 /home/file2", "SSH Client has not been set up");
    }
}
