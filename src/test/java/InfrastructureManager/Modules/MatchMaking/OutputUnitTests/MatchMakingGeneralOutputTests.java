package InfrastructureManager.Modules.MatchMaking.OutputUnitTests;

import InfrastructureManager.Modules.CommonTestingMethods;
import InfrastructureManager.Modules.MatchMaking.Exception.MatchMakingModuleException;
import InfrastructureManager.Modules.MatchMaking.MatchMakerType;
import InfrastructureManager.Modules.MatchMaking.MatchesList;
import InfrastructureManager.Modules.MatchMaking.Output.MatchMakerOutput;
import org.junit.Assert;
import org.junit.Test;

public class MatchMakingGeneralOutputTests {

    private final MatchesList matchesList = new MatchesList();
    private final MatchMakerOutput matchMaker = new MatchMakerOutput("mm", MatchMakerType.RANDOM, matchesList);

    @Test
    public void registerNodeTest() throws MatchMakingModuleException {
        matchMaker.out("matchMaker register_node {\"id\":\"node1\",\"ipAddress\":\"192.168.0.1\",\"connected\":true}");
        String expected = matchMaker.getNodeList().get(0).getId();
        Assert.assertEquals(expected, "node1");
    }

    public void registerMultipleNodesTest() throws MatchMakingModuleException {
        matchMaker.out("matchMaker register_node {\"id\":\"node1\",\"ipAddress\":\"192.168.0.1\",\"connected\":true}");
        matchMaker.out("matchMaker register_node {\"id\":\"node2\",\"ipAddress\":\"255.169.255.10\",\"connected\":false}");
        int size = matchMaker.getNodeList().size();
        Assert.assertEquals(2,size);
    }

    @Test
    public void registerClientTest() throws MatchMakingModuleException {
        matchMaker.out("matchMaker register_client {\"id\":\"client1\"}");
        String expected = matchMaker.getClientList().get(0).getId();
        Assert.assertEquals(expected, "client1");
    }

    @Test
    public void invalidCommandThrowsException() {
        CommonTestingMethods.assertException(IllegalArgumentException.class,
                "Invalid command for MatchMaker", () -> matchMaker.out("matchMaker notACommand"));
    }

    @Test
    public void incompleteCommandThrowsException() {
        CommonTestingMethods.assertException(IllegalArgumentException.class,
                "Arguments missing for command - MatchMaker", () -> matchMaker.out("matchMaker register_client"));
    }
}
