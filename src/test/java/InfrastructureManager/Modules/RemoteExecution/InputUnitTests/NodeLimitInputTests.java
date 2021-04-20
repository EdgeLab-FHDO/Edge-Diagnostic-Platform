package InfrastructureManager.Modules.RemoteExecution.InputUnitTests;

import InfrastructureManager.Configuration.Exception.ConfigurationException;
import InfrastructureManager.Master;
import InfrastructureManager.ModuleManagement.Exception.Creation.ModuleManagerException;
import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleExecutionException;
import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleNotFoundException;
import InfrastructureManager.Modules.AdvantEDGE.AdvantEdgeModule;
import InfrastructureManager.Modules.RemoteExecution.Input.NodeLimitInput;
import InfrastructureManager.Modules.RemoteExecution.Output.NodeLimitOutput;
import InfrastructureManager.Modules.RemoteExecution.RemoteExecutionModule;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class NodeLimitInputTests {

    private static RemoteExecutionModule module ;//= new RemoteExecutionModule();
    private final NodeLimitOutput output = new NodeLimitOutput(module,"limit.out");
    private final NodeLimitInput input = new NodeLimitInput(module, "limit.in");

    @BeforeClass
    public static void setUpMasterAndStartServer() throws ModuleNotFoundException, ConfigurationException, ModuleManagerException {
        Master.resetInstance();
        Master.getInstance().configure("src/test/resources/Modules/RemoteExecution/NodeLimit/NodeLimitConfiguration.json");
        Master.getInstance().getManager().startModule("rest");
        module = (RemoteExecutionModule) Master.getInstance().getManager().getModules().stream()
                .filter(m -> m.getName().equals("remote"))
                .findFirst()
                .orElseThrow();

    }

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
