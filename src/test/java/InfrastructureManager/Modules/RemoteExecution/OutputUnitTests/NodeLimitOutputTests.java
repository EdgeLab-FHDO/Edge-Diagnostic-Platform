package InfrastructureManager.Modules.RemoteExecution.OutputUnitTests;

import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleExecutionException;
import InfrastructureManager.Modules.CommonTestingMethods;
import InfrastructureManager.Modules.RemoteExecution.Exception.NodeLimit.InvalidLimitParametersException;
import InfrastructureManager.Modules.RemoteExecution.Exception.NodeLimit.NodeLimitException;
import InfrastructureManager.Modules.RemoteExecution.LimitList;
import InfrastructureManager.Modules.RemoteExecution.Output.NodeLimitOutput;
import InfrastructureManager.Modules.RemoteExecution.RemoteExecutionModule;
import org.junit.Assert;
import org.junit.Test;

public class NodeLimitOutputTests {

    RemoteExecutionModule module = new RemoteExecutionModule();
    private final LimitList list = new LimitList(module);
    private final NodeLimitOutput output = new NodeLimitOutput(module,"limit.out", list);

    private void assertExceptionInOutput(Class<? extends Exception> exceptionClass, String expectedMessage, String command) {
        CommonTestingMethods.assertException(exceptionClass, expectedMessage, () -> output.execute(command));
    }

    @Test
    public void limitsAreAddedWithDefaultPeriod() throws ModuleExecutionException {
        output.execute("limit cores node1 0.5");
        String expectedLimitForNode1 = "50000_100000";
        Assert.assertEquals(expectedLimitForNode1, list.getLimitList().get("node1"));
    }

    @Test
    public void limitsAreAddedWithCustomPeriod() throws ModuleExecutionException {
        output.execute("limit cores node1 0.5 1000");
        String expectedLimitForNode1 = "500_1000";
        Assert.assertEquals(expectedLimitForNode1, list.getLimitList().get("node1"));
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
