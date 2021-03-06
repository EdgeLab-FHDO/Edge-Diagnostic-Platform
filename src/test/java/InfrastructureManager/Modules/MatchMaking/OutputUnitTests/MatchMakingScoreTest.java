package InfrastructureManager.Modules.MatchMaking.OutputUnitTests;

import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleExecutionException;
import InfrastructureManager.Modules.MatchMaking.Client.EdgeClient;
import InfrastructureManager.Modules.MatchMaking.Client.EdgeClientHistory;
import InfrastructureManager.Modules.MatchMaking.Exception.MatchMakingModuleException;
import InfrastructureManager.Modules.MatchMaking.MatchMakingModule;
import InfrastructureManager.Modules.MatchMaking.Naive.NaiveMatchMaking;
import InfrastructureManager.Modules.MatchMaking.Node.EdgeNode;
import InfrastructureManager.Modules.MatchMaking.Output.MatchMakerOutput;
import InfrastructureManager.Modules.MatchMaking.ScoreBased.ScoreBasedMatchMaking;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MatchMakingScoreTest {

    private final MatchMakingModule module = new MatchMakingModule();
    private final MatchMakerOutput matchMaker = new MatchMakerOutput(module,"mm", new ScoreBasedMatchMaking(module));

    @Before
    public void register3NodesAnd2Clients() throws ModuleExecutionException {
        String node1AsString = "{\"id\":\"node1\",\"ipAddress\":\"68.131.232.215:30968\",\"connected\":true,\"resource\":200,\"totalResource\":200,\"network\":200,\"totalNetwork\":200,\"location\":55,\"heartBeatInterval\":15000}";
        matchMaker.execute("matchMaker register_node " + node1AsString);
        String node2AsString = "{\"id\":\"node2\",\"ipAddress\":\"92.183.84.109:42589\",\"connected\":true,\"resource\":200,\"totalResource\":200,\"network\":200,\"totalNetwork\":200,\"location\":33,\"heartBeatInterval\":15000}";
        matchMaker.execute("matchMaker register_node " + node2AsString);
        String node3AsString = "{\"id\":\"node3\",\"ipAddress\":\"138.134.15.25:25545\",\"connected\":true,\"resource\":200,\"totalResource\":200,\"network\":200,\"totalNetwork\":200,\"location\":77,\"heartBeatInterval\":15000}";
        matchMaker.execute("matchMaker register_node " + node3AsString);
        String client1AsString = "{\"id\":\"client1\",\"reqNetwork\":5,\"reqResource\":10,\"location\":54,\"heartBeatInterval\":15000}";
        matchMaker.execute("matchMaker register_client " + client1AsString);
        String client2AsString = "{\"id\":\"client2\",\"reqNetwork\":20,\"reqResource\":40,\"location\":42,\"heartBeatInterval\":15000}";
        matchMaker.execute("matchMaker register_client " + client2AsString);
    }


    @Test
    public void nodeListUpdatesCorrectlyTest() throws ModuleExecutionException {
        //Changed IP
        String modifiedNode1AsString = "{\"id\":\"node1\",\"ipAddress\":\"40.16.64.123:30968\",\"connected\":true,\"resource\":200,\"totalResource\":200,\"network\":200,\"totalNetwork\":200,\"location\":55,\"heartBeatInterval\":15000}";
        matchMaker.execute("matchMaker register_node " + modifiedNode1AsString);
        Assert.assertEquals(3, matchMaker.getNodeList().size()); //Size is still 3;
        EdgeNode node1 = matchMaker.getNodeList().get(0);
        Assert.assertEquals("40.16.64.123:30968", node1.getIpAddress());
    }

    @Test
    public void clientListUpdatesCorrectlyTest() throws ModuleExecutionException {
        //Changed reqNetwork 5 -> 10
        String modifiedClient1AsString = "{\"id\":\"client1\",\"reqNetwork\":10,\"reqResource\":10,\"location\":54,\"heartBeatInterval\":15000}";
        matchMaker.execute("matchMaker register_client " + modifiedClient1AsString);
        Assert.assertEquals(2,matchMaker.getClientList().size());
        EdgeClient client1 = matchMaker.getClientList().get(0);
        Assert.assertEquals(10,client1.getReqNetwork());
    }

    @Test
    public void assignCorrectlyNodeToClientTest() throws Exception {

        matchMaker.execute("matchMaker assign_client client1");
        //client1 should be mapped to node1
//        String thisShouldBeNode1 = getNodeIDFromJSON(module.getSharedList().getMapping().get("client1"));
        String thisShouldBeNode1 = module.getSharedList().getMapping().get("client1");
        String expected = "{\"id\":\"node1\",\"ipAddress\":\"68.131.232.215:30968\",\"connected\":true,\"resource\":200,\"network\":200,\"location\":55,\"totalResource\":200,\"totalNetwork\":200,\"heartBeatInterval\":15000,\"online\":true,\"watchDogOnline\":true}";
        Assert.assertEquals(expected, thisShouldBeNode1);
    }

//    @Ignore("Currently broken")
    @Test
    public void disconnectAndChangeInScoreTest() throws Exception {
        matchMaker.execute("matchMaker assign_client client1");
        //job failed -> client 1 score with node 1 should be 10
        matchMaker.execute("matchMaker disconnect_client {\"id\":\"client1\",\"message\":\"job_failed\"}");
        EdgeClientHistory client1History = matchMaker.getClientList().get(0).getClientHistory();
        long thisShouldBe10  = client1History.getHistoryScore("node1");
        Assert.assertEquals(10,thisShouldBe10);
        //wait 2s to see history change ( from 10 score -> 8 score )
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //Re assign client 1
        matchMaker.execute("matchMaker assign_client client1");
        //2s passed, clint1-node1 score should be 8
        long thisShouldBe8  =  matchMaker.getClientList().get(0).getClientHistory().getHistoryScore("node1");
        Assert.assertEquals(8,thisShouldBe8);

        //Client 1 should connect to node 2 now cuz node 2 have better score
//        String thisShouldBeNode2 = getNodeIDFromJSON(module.getSharedList().getMapping().get("client1"));
        String thisShouldBeNode2 = module.getSharedList().getMapping().get("client1");
        String expected2 = "{\"id\":\"node2\",\"ipAddress\":\"92.183.84.109:42589\",\"connected\":true,\"resource\":200,\"network\":200,\"location\":33,\"totalResource\":200,\"totalNetwork\":200,\"heartBeatInterval\":15000,\"online\":true,\"watchDogOnline\":true}";
        Assert.assertEquals(expected2,thisShouldBeNode2);
    }

    @Test
    public void heartBeatTest() throws MatchMakingModuleException {
        MatchMakerOutput output = new MatchMakerOutput(module,"mm.out", new NaiveMatchMaking(module));
        String node = "{\"id\":\"node5\",\"ipAddress\":\"68.131.232.215:30968\",\"connected\":true,\"resource\":100,\"totalResource\":200,\"network\":100,\"totalNetwork\":200,\"location\":55,\"heartBeatInterval\":5000}";
        String client = "{\"id\":\"client5\",\"reqNetwork\":5,\"reqResource\":10,\"location\":54,\"heartBeatInterval\":5000}";

        matchMaker.execute("matchMaker register_node " + node);
        matchMaker.execute("matchMaker register_client " + client);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //if we get this msg [Index -1 out of bounds for length 3] then there is no node5, check the code again
        int node5Index = -1;
        List<EdgeNode> nodeList = matchMaker.getNodeList();
        for (EdgeNode thisNode : nodeList){
            if (thisNode.getId().equals("node5")){
                node5Index = nodeList.indexOf(thisNode);
                break;
            }
        }

        int client5Index = -1;
        List<EdgeClient> clientList = matchMaker.getClientList();
        for (EdgeClient thisClient : clientList){
            if (thisClient.getId().equals("client5")){
                client5Index = clientList.indexOf(thisClient);
                break;
            }
        }
        boolean nodeShouldBeOnline = matchMaker.getNodeList().get(node5Index).isOnline();
        boolean clientShouldBeOnline = matchMaker.getClientList().get(client5Index).isOnline();
        //Node and client should still be online, heart beat is deadline is 5s, only waited for 1s
        Assert.assertEquals(true,nodeShouldBeOnline);
        Assert.assertEquals(true,clientShouldBeOnline);

        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        nodeShouldBeOnline = matchMaker.getNodeList().get(node5Index).isOnline();
        clientShouldBeOnline = matchMaker.getClientList().get(client5Index).isOnline();

        //Node and client should be offline now, heart beat is deadline is 5s, waited for 6s
        Assert.assertEquals(false,nodeShouldBeOnline);
        Assert.assertEquals(false,clientShouldBeOnline);
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
