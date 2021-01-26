package InfrastructureManager.Modules.RemoteExecution.InputUnitTests;

import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleExecutionException;
import InfrastructureManager.Modules.RemoteExecution.Input.NodeLimitInput;
import InfrastructureManager.Modules.RemoteExecution.LimitList;
import InfrastructureManager.Modules.RemoteExecution.Output.NodeLimitOutput;
import InfrastructureManager.Modules.RemoteExecution.RemoteExecutionModule;
import org.junit.Assert;
import org.junit.Test;

public class NodeLimitInputTests {

    private final LimitList list = new LimitList();
    RemoteExecutionModule module = new RemoteExecutionModule();
    private final NodeLimitOutput output = new NodeLimitOutput(module,"limit.out", list);
    private final NodeLimitInput input = new NodeLimitInput(module, "limit.in", list);

    @Test
    public void limitBodyIsCorrectWithDefaultPeriod() throws ModuleExecutionException, InterruptedException {
        output.execute("limit cores node1 0.7");
        String expected = "set_limits {\"node1\":\"70000_100000\"}";
        Assert.assertEquals(expected, input.read());
    }

    @Test
    public void limitBodyIsCorrectWithCustomPeriod() throws ModuleExecutionException, InterruptedException {
        output.execute("limit cores node1 0.7 1000");
        String expected = "set_limits {\"node1\":\"700_1000\"}";
        Assert.assertEquals(expected, input.read());
    }

    @Test
    public void inputBlocksWhileALimitIsNotSet() throws InterruptedException {
        Thread inputRunner = new Thread(() -> {
            try {
                input.read();
            } catch (InterruptedException ignored) {}
        });
        inputRunner.start();
        Thread.sleep(5000); //Wait 5 seconds, and then check thread is still blocked
        Assert.assertEquals(Thread.State.WAITING, inputRunner.getState());
        inputRunner.interrupt();
    }

}
