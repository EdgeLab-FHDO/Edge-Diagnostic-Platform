package InfrastructureManager.Modules.MatchMaking.OutputUnitTests;

import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleExecutionException;
import InfrastructureManager.Modules.CommonTestingMethods;
import InfrastructureManager.Modules.MatchMaking.Exception.MatchMakingModuleException;
import InfrastructureManager.Modules.MatchMaking.MatchMakingModule;
import InfrastructureManager.Modules.MatchMaking.Output.MatchMakerOutput;
import InfrastructureManager.Modules.MatchMaking.Random.RandomMatchMaking;
import org.junit.Assert;
import org.junit.Test;

public class MatchMakingGeneralOutputTests {

    private final MatchMakingModule module = new MatchMakingModule();
    private final MatchMakerOutput matchMaker = new MatchMakerOutput(module,"mm", new RandomMatchMaking(module));

    @Test
    public void registerNodeTest() throws ModuleExecutionException {
        matchMaker.execute("matchMaker register_node {\"id\":\"node1\",\"ipAddress\":\"192.168.0.1\",\"connected\":true}");
        String expected = matchMaker.getNodeList().get(0).getId();
        Assert.assertEquals(expected, "node1");
    }

    @Test
    public void registerMultipleNodesTest() throws ModuleExecutionException {
        matchMaker.execute("matchMaker register_node {\"id\":\"node1\",\"ipAddress\":\"192.168.0.1\",\"connected\":true}");
        matchMaker.execute("matchMaker register_node {\"id\":\"node2\",\"ipAddress\":\"255.169.255.10\",\"connected\":false}");
        int size = matchMaker.getNodeList().size();
        Assert.assertEquals(2,size);
    }

    @Test
    public void registerClientTest() throws ModuleExecutionException {
        matchMaker.execute("matchMaker register_client {\"id\":\"client1\"}");
        String expected = matchMaker.getClientList().get(0).getId();
        Assert.assertEquals(expected, "client1");
    }

    @Test
    public void invalidCommandThrowsException() {
        String command = "matchMaker notACommand";
        String expected = "Invalid command notACommand for MatchMaker";
        CommonTestingMethods.assertException(MatchMakingModuleException.class,expected, () -> matchMaker.execute(command));
    }

    @Test
    public void incompleteCommandThrowsException() {
        String command = "matchMaker register_client";
        String expected = "Arguments missing for command " + command + " for MatchMakerOutput";
        CommonTestingMethods.assertException(MatchMakingModuleException.class, expected, () -> matchMaker.execute(command));
    }
}
