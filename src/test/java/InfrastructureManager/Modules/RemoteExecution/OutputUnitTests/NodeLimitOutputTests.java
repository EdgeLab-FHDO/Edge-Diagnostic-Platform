package InfrastructureManager.Modules.RemoteExecution.OutputUnitTests;

import InfrastructureManager.Configuration.Exception.ConfigurationException;
import InfrastructureManager.Master;
import InfrastructureManager.ModuleManagement.Exception.Creation.ModuleManagerException;
import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleExecutionException;
import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleNotFoundException;
import InfrastructureManager.Modules.CommonTestingMethods;
import InfrastructureManager.Modules.RemoteExecution.Exception.NodeLimit.InvalidLimitParametersException;
import InfrastructureManager.Modules.RemoteExecution.Exception.NodeLimit.NodeLimitException;
import InfrastructureManager.Modules.RemoteExecution.LimitList;
import InfrastructureManager.Modules.RemoteExecution.Output.NodeLimitOutput;
import InfrastructureManager.Modules.RemoteExecution.RemoteExecutionModule;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class NodeLimitOutputTests {

    private static RemoteExecutionModule module ;
    private final LimitList list = module.getLimitList();
    private final NodeLimitOutput output = new NodeLimitOutput(module,"limit.out");

    private void assertExceptionInOutput(Class<? extends Exception> exceptionClass, String expectedMessage, String command) {
        CommonTestingMethods.assertException(exceptionClass, expectedMessage, () -> output.execute(command));
    }

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
    public void limitsAreAddedWithDefaultPeriod() throws ModuleExecutionException {
        output.execute("limit cores node1 0.5");
        String expectedLimitForNode1 = "50000_100000";
        Assert.assertEquals(expectedLimitForNode1, list.getList().get("node1"));
    }

    @Test
    public void limitsAreAddedWithCustomPeriod() throws ModuleExecutionException {
        output.execute("limit cores node1 0.5 1000");
        String expectedLimitForNode1 = "500_1000";
        Assert.assertEquals(expectedLimitForNode1, list.getList().get("node1"));
    }

    @Test
    public void invalidCommandThrowsException() {
        String command = "limit notACommand";
        String expected = "Invalid command notACommand for NodeLimiter";
        assertExceptionInOutput(NodeLimitException.class, expected, command);
    }

    @Test
    public void incompleteCommandThrowsException() {
        String command = "limit cores";
        String expected = "Arguments missing for command " + command  + " to NodeLimiter";
        assertExceptionInOutput(NodeLimitException.class, expected, command);
    }

    @Test
    public void invalidParametersThrowException() {
        String command = "limit cores node1 x"; //String x instead of floating point value
        String expected = "Parameters to set limits were invalid";
        assertExceptionInOutput(InvalidLimitParametersException.class, expected, command);
    }
}
