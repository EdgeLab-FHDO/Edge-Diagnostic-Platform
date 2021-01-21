package InfrastructureManager.Modules.MatchMaking.InputUnitTests;

import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleExecutionException;
import InfrastructureManager.Modules.MatchMaking.Input.MatchMakerInput;
import InfrastructureManager.Modules.MatchMaking.MatchMakerType;
import InfrastructureManager.Modules.MatchMaking.MatchesList;
import InfrastructureManager.Modules.MatchMaking.Output.MatchMakerOutput;
import org.junit.Assert;
import org.junit.Test;

public class MatchMakingInputTests {

    private final MatchesList matchesList = new MatchesList();
    private final MatchMakerInput input = new MatchMakerInput("mm.in",matchesList);

    @Test
    public void correctInputReadWithRandomMatchMakerOutputTest() throws InterruptedException, ModuleExecutionException {
        MatchMakerOutput output = new MatchMakerOutput("mm.out", MatchMakerType.RANDOM,matchesList);
        String node = "{\"id\":\"node1\",\"ipAddress\":\"192.168.0.1\",\"connected\":true}";
        String client = "{\"id\":\"client1\"}";
        registerNodeAndClient(output,node,client);
        output.write("matchMaker assign_client client1");

        String expected = "give_node client1 {\"id\":\"node1\",\"ipAddress\":\"192.168.0.1\",\"connected\":true,\"resource\":0,\"network\":0,\"location\":0,\"totalResource\":0,\"totalNetwork\":0}";
        Assert.assertEquals(expected,input.read());
    }

    @Test
    public void correctInputReadWithNaiveMatchMakerOutputTest() throws InterruptedException, ModuleExecutionException {
        MatchMakerOutput output = new MatchMakerOutput("mm.out", MatchMakerType.NAIVE,matchesList);
        String node = "{\"id\":\"node1\",\"ipAddress\":\"68.131.232.215:30968\",\"connected\":true,\"resource\":100,\"totalResource\":200,\"network\":100,\"totalNetwork\":200,\"location\":55}";
        String client = "{\"id\":\"client1\",\"reqNetwork\":5,\"reqResource\":10,\"location\":54}";
        registerNodeAndClient(output,node,client);
        output.write("matchMaker assign_client client1");

        String expected = "give_node client1 {\"id\":\"node1\",\"ipAddress\":\"68.131.232.215:30968\",\"connected\":true,\"resource\":200,\"network\":200,\"location\":55,\"totalResource\":200,\"totalNetwork\":200}";
        Assert.assertEquals(expected,input.read());
    }

    @Test
    public void correctInputReadWithScoreMatchMakerOutputTest() throws InterruptedException, ModuleExecutionException {
        MatchMakerOutput output = new MatchMakerOutput("mm.out", MatchMakerType.SCORE_BASED,matchesList);
        String node = "{\"id\":\"node1\",\"ipAddress\":\"68.131.232.215:30968\",\"connected\":true,\"resource\":100,\"totalResource\":200,\"network\":100,\"totalNetwork\":200,\"location\":55}";
        String client = "{\"id\":\"client1\",\"reqNetwork\":5,\"reqResource\":10,\"location\":54}";
        registerNodeAndClient(output,node,client);
        output.write("matchMaker assign_client client1");

        String expected = "give_node client1 {\"id\":\"node1\",\"ipAddress\":\"68.131.232.215:30968\",\"connected\":true,\"resource\":200,\"network\":200,\"location\":55,\"totalResource\":200,\"totalNetwork\":200}";
        Assert.assertEquals(expected,input.read());
    }

    private void registerNodeAndClient(MatchMakerOutput output, String nodeAsString, String clientAsString) throws ModuleExecutionException {
        output.write("matchMaker register_node " + nodeAsString);
        output.write("matchMaker register_client " + clientAsString);
    }
}
