package InfrastructureManager.SSH;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class SSHClientTests {

    private final SSHClient client = new SSHClient("test_ssh");

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void invalidCommandThrowsException() {
        exceptionRule.expect(IllegalArgumentException.class);
        exceptionRule.expectMessage("Invalid command for SSHClient");
        client.out("ssh notACommand");
    }

    @Test
    public void incompleteCommandThrowsException() {
        exceptionRule.expect(IllegalArgumentException.class);
        exceptionRule.expectMessage("Arguments missing for command - SSHClient");
        client.out("ssh setup"); //Missing the parameters
    }

    @Test
    public void executeCommandNeedsToHaveDisplayFlag() {
        exceptionRule.expect(IllegalArgumentException.class);
        exceptionRule.expectMessage("Wrong Display parameter in SSHClient execute command");
        client.out("ssh execute ls"); //Missing the parameters
    }

    @Test
    public void clientHasToBeSetUpBeforeExecuting(){
        exceptionRule.expect(IllegalStateException.class);
        exceptionRule.expectMessage("SSH Client has not been set up");
        client.out("ssh execute -f ls");
    }

    @Test
    public void clientHasToBeSetUpBeforeSendingFiles(){
        exceptionRule.expect(IllegalStateException.class);
        exceptionRule.expectMessage("SSH Client has not been set up");
        client.out("ssh sendFile file1 /home/file2");
    }




}
