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

        matchMaker.execute("matchMaker register_node {\"id\":\"node1\",\"ipAddress\":\"68.131.232.215:30968\",\"connected\":true,\"resource\":200,\"totalResource\":200,\"network\":200,\"totalNetwork\":200,\"location\":55,\"heartBeatInterval\":15000}");
        matchMaker.execute("matchMaker register_client {\"id\":\"client1\",\"reqNetwork\":5,\"reqResource\":10,\"location\":54,\"heartBeatInterval\":15000}");
        matchMaker.execute("matchMaker assign_client client1");
        String result = module.getSharedList().getLastAdded();
        String expected = "client1 {\"id\":\"node1\",\"ipAddress\":\"68.131.232.215:30968\",\"connected\":true,\"resource\":200,\"network\":200,\"location\":55,\"totalResource\":200,\"totalNetwork\":200,\"heartBeatInterval\":15000,\"online\":true,\"watchDogOnline\":true}";
//        String expected = "client1 node1";
        Assert.assertEquals(expected,result);
    }
}

