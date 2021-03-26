package InfrastructureManager.Modules.MatchMaking.OutputUnitTests;

import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleExecutionException;
import InfrastructureManager.Modules.MatchMaking.MatchMakingModule;
import InfrastructureManager.Modules.MatchMaking.Output.MatchMakerOutput;
import InfrastructureManager.Modules.MatchMaking.Random.RandomMatchMaking;
import org.junit.Assert;
import org.junit.Test;

public class MatchMakingRandomTests {

    private final MatchMakingModule module = new MatchMakingModule();
    private final MatchMakerOutput matchMaker = new MatchMakerOutput(module,"mm", new RandomMatchMaking(module));

    @Test
    public void assignNodeToClientCompleteTest() throws InterruptedException, ModuleExecutionException {

        matchMaker.execute("matchMaker register_node {\"id\":\"node1\",\"ipAddress\":\"192.168.0.1\",\"connected\":true}");
        matchMaker.execute("matchMaker register_client {\"id\":\"client1\"}");
        matchMaker.execute("matchMaker assign_client client1");
        String result = module.getSharedList().getLastAdded();
//        String expected = "client1 {\"id\":\"node1\",\"ipAddress\":\"192.168.0.1\",\"connected\":true,\"resource\":0,\"network\":0,\"location\":0,\"totalResource\":0,\"totalNetwork\":0}";
        String expected = "client1 node1";
        Assert.assertEquals(expected,result);
    }
}

