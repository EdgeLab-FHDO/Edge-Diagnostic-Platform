package InfrastructureManager;

import org.junit.Assert;
import org.junit.Test;

public class MasterMappingTests {
    Master master = Master.getInstance();

    @Test
    public void command1Test() {
        String command = "deploy_application";
        String expected = "Helm chart execution";
        Assert.assertEquals(expected,master.execute(command));
    }
    @Test
    public void command2Test() {
        String command = "node_request";
        String expected = "Match making execution";
        Assert.assertEquals(expected,master.execute(command));
    }
    @Test
    public void command3Test() {
        String command = "update_gui";
        String expected = "GUI update execution";
        Assert.assertEquals(expected,master.execute(command));
    }
    @Test
    public void command4Test() {
        String command = "save_statistics";
        String expected = "Logger execution";
        Assert.assertEquals(expected,master.execute(command));
    }
    @Test
    public void command5Test() {
        String command = "show_statistics";
        String expected = "GUI update execution";
        Assert.assertEquals(expected,master.execute(command));
    }
    @Test
    public void command6Test() {
        String command = "create_scenario NAME";
        String expected = "create NAME";
        Assert.assertEquals(expected,master.execute(command));
    }
    @Test
    public void command7Test() {
        String command = "add_event COMMAND 0";
        String expected = "addEvent COMMAND 0";
        Assert.assertEquals(expected,master.execute(command));
    }
    @Test
    public void command8Test() {
        String command = "delete_event";
        String expected = "deleteEvent";
        Assert.assertEquals(expected,master.execute(command));
    }
    @Test
    public void command9Test() {
        String command = "save_scenario PATH";
        String expected = "toFile PATH";
        Assert.assertEquals(expected,master.execute(command));
    }
    @Test
    public void command10Test() {
        String command = "load_scenario PATH";
        String expected = "fromFile PATH";
        Assert.assertEquals(expected,master.execute(command));
    }
    @Test
    public void commandNotRecognizedTest() {
        String command = "do_something";
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
