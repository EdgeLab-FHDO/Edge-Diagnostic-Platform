package InfrastructureManager;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;


public class MasterMappingTests {
    Master master = Master.getInstance();

    @Test
    public void command1Test() {
        String command = "deploy_application";
        String expected = "console helmChartExecution";
        Assert.assertEquals(expected,master.execute(command));
    }
    @Test
    public void command2Test() {
        String command = "node_request";
        String expected = "console matchMakingExecution";
        Assert.assertEquals(expected,master.execute(command));
    }
    @Test
    public void command3Test() {
        String command = "update_gui";
        String expected = "console GUIUpdateExecution";
        Assert.assertEquals(expected,master.execute(command));
    }
    @Test
    public void command4Test() {
        String command = "save_statistics";
        String expected = "console LoggerExecution";
        Assert.assertEquals(expected,master.execute(command));
    }
    @Test
    public void command5Test() {
        String command = "show_statistics";
        String expected = "console GUIUpdateExecution";
        Assert.assertEquals(expected,master.execute(command));
    }
    @Test
    public void command6Test() {
        String command = "create_scenario NAME";
        String expected = "editor create NAME";
        Assert.assertEquals(expected,master.execute(command));
    }
    @Test
    public void command7Test() {
        String command = "add_event COMMAND";
        String expected = "editor addEvent COMMAND";
        Assert.assertEquals(expected,master.execute(command));
    }
    @Test
    public void command8Test() {
        String command = "delete_event";
        String expected = "editor deleteEvent";
        Assert.assertEquals(expected,master.execute(command));
    }
    @Test
    public void command9Test() {
        String command = "save_scenario PATH";
        String expected = "editor toFile PATH";
        Assert.assertEquals(expected,master.execute(command));
    }
    @Test
    public void command10Test() {
        String command = "read_scenario PATH";
        String expected = "editor fromFile PATH";
        Assert.assertEquals(expected,master.execute(command));
    }
    @Test
    public void command11Test() {
        String command = "load_scenario PATH";
        String expected = "dispatcher fromFile PATH";
        Assert.assertEquals(expected,master.execute(command));
    }
    @Test
    public void command12Test() {
        String command = "run_scenario";
        String expected = "dispatcher run";
        Assert.assertEquals(expected,master.execute(command));
    }
    @Test
    public void command13Test() {
        String command = "pause_scenario";
        String expected = "dispatcher pause";
        Assert.assertEquals(expected,master.execute(command));
    }
    @Test
    public void command14Test() {
        String command = "resume_scenario";
        String expected = "dispatcher resume";
        Assert.assertEquals(expected,master.execute(command));
    }
    @Test
    public void giveNodeTest() {
        String command = "give_node client1 node1";
        String expected = "restOut sendNode client1 node1";
        Assert.assertEquals(expected,master.execute(command));
    }
    @Test
    public void registerClientTest() {
        String command = "register_client $clientAsJSON";
        String expected = "matchMaker register_client $clientAsJSON";
        Assert.assertEquals(expected,master.execute(command));
    }
    @Test
    public void registerNodeTest() {
        String command = "register_node $nodeAsJSON";
        String expected = "matchMaker register_node $nodeAsJSON";
        Assert.assertEquals(expected,master.execute(command));
    }
    @Test
    public void exitTest() {
        String command = "exit";
        String expected = "util exit";
        Assert.assertEquals(expected,master.execute(command));
    }

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void commandEmptyThrowsExceptionTest() {
        exceptionRule.expect(IllegalArgumentException.class);
        exceptionRule.expectMessage("Empty Command at input!");
        String command = "";
        master.execute(command);
    }

    @Test
    public void commandNotRecognizedThrowsExceptionTest() {
        exceptionRule.expect(IllegalArgumentException.class);
        exceptionRule.expectMessage("Command not defined in config");
        String command = "do_something";
        master.execute(command);
    }
}
