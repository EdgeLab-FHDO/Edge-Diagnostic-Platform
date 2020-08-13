package InfrastructureManager;

import org.junit.Assert;
import org.junit.Test;

public class MasterTests {
    Master master = new Master();

    @Test
    public void command1Test() {
        String command = "deploy application";
        String expected = "Helm chart execution";
        Assert.assertEquals(expected,master.execute(command));
    }
    @Test
    public void command2Test() {
        String command = "node request";
        String expected = "Match making execution";
        Assert.assertEquals(expected,master.execute(command));
    }
    @Test
    public void command3Test() {
        String command = "update gui";
        String expected = "GUI update execution";
        Assert.assertEquals(expected,master.execute(command));
    }
    @Test
    public void command4Test() {
        String command = "save statistics";
        String expected = "Logger execution";
        Assert.assertEquals(expected,master.execute(command));
    }
    @Test
    public void command5Test() {
        String command = "show statistics";
        String expected = "GUI update execution";
        Assert.assertEquals(expected,master.execute(command));
    }
    @Test
    public void commandNotRecognizedTest() {
        String command = "do something";
        String expected = "command not defined!";
        Assert.assertEquals(expected,master.execute(command));
    }

    @Test
    public void commandEmptyTest() {
        String command = "";
        String expected = "Empty Command!";
        Assert.assertEquals(expected,master.execute(command));
    }


}
