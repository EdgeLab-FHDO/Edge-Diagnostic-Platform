package InfrastructureManager.Modules.MatchMaking.InputUnitTests;

import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleExecutionException;
import InfrastructureManager.Modules.MatchMaking.Input.MatchMakerInput;
import InfrastructureManager.Modules.MatchMaking.MatchMakingModule;
import InfrastructureManager.Modules.MatchMaking.MatchesList;
import InfrastructureManager.Modules.MatchMaking.Naive.NaiveMatchMaking;
import InfrastructureManager.Modules.MatchMaking.Output.MatchMakerOutput;
import InfrastructureManager.Modules.MatchMaking.Random.RandomMatchMaking;
import InfrastructureManager.Modules.MatchMaking.ScoreBased.ScoreBasedMatchMaking;
import org.junit.Assert;
import org.junit.Test;

public class MatchMakingInputTests {

    private final MatchMakingModule module = new MatchMakingModule(); //Generic Module
    private final MatchMakerInput input = new MatchMakerInput(module, "mm.in");

    @Test
    public void correctInputReadWithRandomMatchMakerOutputTest() throws InterruptedException, ModuleExecutionException {
        MatchMakerOutput output = new MatchMakerOutput(module,"mm.out", new RandomMatchMaking(module));
        String node = "{\"id\":\"node1\",\"ipAddress\":\"192.168.0.1\",\"connected\":true}";
        String client = "{\"id\":\"client1\"}";
        registerNodeAndClient(output,node,client);
        output.execute("matchMaker assign_client client1");
        String expected = "give_node client1 node1";
        Assert.assertEquals(expected,input.read());

    }

    @Test
    public void correctInputReadWithNaiveMatchMakerOutputTest() throws InterruptedException, ModuleExecutionException {
        MatchMakerOutput output = new MatchMakerOutput(module,"mm.out", new NaiveMatchMaking(module));
        String node = "{\"id\":\"node1\",\"ipAddress\":\"68.131.232.215:30968\",\"connected\":true,\"resource\":100,\"totalResource\":200,\"network\":100,\"totalNetwork\":200,\"location\":55,\"heartBeatInterval\":15000}";
        String client = "{\"id\":\"client1\",\"reqNetwork\":5,\"reqResource\":10,\"location\":54,\"heartBeatInterval\":15000}";
        registerNodeAndClient(output,node,client);
        output.execute("matchMaker assign_client client1");
        String expected = "give_node client1 node1";
        Assert.assertEquals(expected,input.read());
    }

    @Test
    public void correctInputReadWithScoreMatchMakerOutputTest() throws InterruptedException, ModuleExecutionException {
        MatchMakerOutput output = new MatchMakerOutput(module,"mm.out", new ScoreBasedMatchMaking(module));
        String node = "{\"id\":\"node1\",\"ipAddress\":\"68.131.232.215:30968\",\"connected\":true,\"resource\":100,\"totalResource\":200,\"network\":100,\"totalNetwork\":200,\"location\":55}";
        String client = "{\"id\":\"client1\",\"reqNetwork\":5,\"reqResource\":10,\"location\":54}";
        registerNodeAndClient(output,node,client);
        output.execute("matchMaker assign_client client1");

        String expected = "give_node client1 node1";
        Assert.assertEquals(expected,input.read());
    }

    private void registerNodeAndClient(MatchMakerOutput output, String nodeAsString, String clientAsString) throws ModuleExecutionException {
        output.execute("matchMaker register_node " + nodeAsString);
        output.execute("matchMaker register_client " + clientAsString);
    }
}
