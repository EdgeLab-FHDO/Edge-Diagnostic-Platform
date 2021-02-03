package InfrastructureManager.Modules.MatchMaking.OutputUnitTests;

import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleExecutionException;
import InfrastructureManager.Modules.MatchMaking.Client.EdgeClient;
import InfrastructureManager.Modules.MatchMaking.MatchMakingModule;
import InfrastructureManager.Modules.MatchMaking.MatchesList;
import InfrastructureManager.Modules.MatchMaking.Naive.NaiveMatchMaking;
import InfrastructureManager.Modules.MatchMaking.Node.EdgeNode;
import InfrastructureManager.Modules.MatchMaking.Output.MatchMakerOutput;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MatchMakingNaiveTest {

    private final MatchesList matchesList = new MatchesList();
    private final MatchMakingModule module = new MatchMakingModule();
    private final MatchMakerOutput matchMaker = new MatchMakerOutput(module,"mm", new NaiveMatchMaking(), matchesList);

    @Before
    public void register3NodesAnd2Clients() throws ModuleExecutionException {
        String node1AsString = "{\"id\":\"node1\",\"ipAddress\":\"68.131.232.215:30968\",\"connected\":true,\"resource\":100,\"totalResource\":200,\"network\":100,\"totalNetwork\":200,\"location\":55}";
        matchMaker.execute("matchMaker register_node " + node1AsString);
        String node2AsString = "{\"id\":\"node2\",\"ipAddress\":\"92.183.84.109:42589\",\"connected\":true,\"resource\":100,\"totalResource\":200,\"network\":100,\"totalNetwork\":200,\"location\":33}";
        matchMaker.execute("matchMaker register_node " + node2AsString);
        String node3AsString = "{\"id\":\"node3\",\"ipAddress\":\"138.134.15.25:25545\",\"connected\":true,\"resource\":100,\"totalResource\":200,\"network\":100,\"totalNetwork\":200,\"location\":77}";
        matchMaker.execute("matchMaker register_node " + node3AsString);
        String client1AsString = "{\"id\":\"client1\",\"reqNetwork\":5,\"reqResource\":10,\"location\":54}";
        matchMaker.execute("matchMaker register_client " + client1AsString);
        String client2AsString = "{\"id\":\"client2\",\"reqNetwork\":20,\"reqResource\":40,\"location\":42}";
        matchMaker.execute("matchMaker register_client " + client2AsString);
    }

    @Test
    public void nodeListUpdatesCorrectlyTest() throws ModuleExecutionException {
        //Changed IP
        String modifiedNode1AsString = "{\"id\":\"node1\",\"ipAddress\":\"186.173.74.217:30968\",\"connected\":true,\"resource\":100,\"totalResource\":200,\"network\":100,\"totalNetwork\":200,\"location\":55}";
        matchMaker.execute("matchMaker register_node " + modifiedNode1AsString);
        Assert.assertEquals(3, matchMaker.getNodeList().size()); //Size is still 3;
        EdgeNode node1 = matchMaker.getNodeList().get(0);
        Assert.assertEquals("186.173.74.217:30968", node1.getIpAddress());
    }

    @Test
    public void clientListUpdatesCorrectlyTest() throws ModuleExecutionException {
        //Changed reqResource 10 -> 20
        String modifiedClient1AsString = "{\"id\":\"client1\",\"reqNetwork\":5,\"reqResource\":20,\"location\":54}";
        matchMaker.execute("matchMaker register_client " + modifiedClient1AsString);
        Assert.assertEquals(2,matchMaker.getClientList().size());
        EdgeClient client1 = matchMaker.getClientList().get(0);
        Assert.assertEquals(20,client1.getReqResource());
    }

    @Test
    public void assignCorrectlyNodeToClientTest() throws Exception {

        matchMaker.execute("matchMaker assign_client client1");
        //client1 should be mapped to node1 because is closer
        String thisShouldBeNode1 = getNodeIDFromJSON(matchesList.getMapping().get("client1"));
        Assert.assertEquals("node1", thisShouldBeNode1);

        matchMaker.execute("matchMaker assign_client client2");
        String thisShouldBeNode2 = getNodeIDFromJSON(matchesList.getMapping().get("client2"));
        //Should be node 2 because node2 is closer to client 2 than others
        Assert.assertEquals("node2", thisShouldBeNode2);
    }

    @Test
    public void oneClientDisconnectAndReassignTest() throws Exception {
        matchMaker.execute("matchMaker assign_client client1");
        //Disconnect node1, ready for re assign
        matchMaker.execute("matchMaker disconnect_client {\"id\":\"client1\",\"message\":\"job_failed\"}");
        //Re assign client 1
        matchMaker.execute("matchMaker assign_client client1");
        //Should still be node 1, the distance hasn't changed yet
        String thisShouldBeNode1 = getNodeIDFromJSON(matchesList.getMapping().get("client1"));
        Assert.assertEquals("node1", thisShouldBeNode1);
    }

    @Test
    public void disconnectAndUpdateTest() throws Exception {
        matchMaker.execute("matchMaker assign_client client2");
        //Disconnect client 2
        matchMaker.execute("matchMaker disconnect_client {\"id\":\"client2\",\"message\":\"job_failed\"}");
        //update node2 so it will be damn far away from client 2, making client 2 must connect to node 1
        matchMaker.execute("matchMaker register_node {\"id\":\"node2\",\"ipAddress\":\"92.183.84.109:42589\",\"connected\":true,\"resource\":100,\"totalResource\":200,\"network\":100,\"totalNetwork\":200,\"location\":133}");
        matchMaker.execute("matchMaker assign_client client2");
        String thisShouldBeNode1ForThis = getNodeIDFromJSON(matchesList.getMapping().get("client2"));
        //Should be node 1 because node1 is closer to client 2 than others
        Assert.assertEquals("node1", thisShouldBeNode1ForThis);
    }

    private String getNodeIDFromJSON(String nodeAsString) throws Exception {
        Pattern pattern = Pattern.compile("(?<=\"id\":\")\\w+");
        Matcher matcher = pattern.matcher(nodeAsString);
        if (matcher.find()) {
            return matcher.group();
        } else {
            throw new Exception("Incorrect node body");
        }
    }

}
