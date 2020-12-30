package InfrastructureManager.MatchMaking;

import InfrastructureManager.Configuration.CommandSet;
import InfrastructureManager.EdgeClientHistory;
import InfrastructureManager.EdgeNode;
import InfrastructureManager.Master;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.*;
import org.junit.rules.ExpectedException;



public class MatchMakingScoreTest {

    private final MatchMaker matchMaker = new MatchMaker("match_m", "score");
    private final CommandSet commandSet = new CommandSet();
    private static RequestSpecification requestSpec;

    @BeforeClass
    public static void startServer() { //Before all tests
        String testIp = "http://localhost";
        int port = 4567;
        requestSpec = new RequestSpecBuilder().
                setBaseUri(testIp).
                setPort(port).
                build();
        Master.resetInstance();
        Master.changeConfigPath("src/test/resources/MatchMaking/MatchMakingConfiguration.json");
        Master.getInstance().startRunnerThread("RestServer");
    }

    @Test
    public void registerAndUpdateNodeTest() throws Exception {
        //Register 3 node
        matchMaker.out("matchMaker register_node {\"id\":\"node1\",\"ipAddress\":\"68.131.232.215:30968\",\"connected\":true,\"resource\":100,\"totalResource\":200,\"network\":100,\"totalNetwork\":200,\"location\":55}");
        matchMaker.out("matchMaker register_node {\"id\":\"node2\",\"ipAddress\":\"92.183.84.109:42589\",\"connected\":true,\"resource\":100,\"totalResource\":200,\"network\":100,\"totalNetwork\":200,\"location\":33}");
        matchMaker.out("matchMaker register_node {\"id\":\"node3\",\"ipAddress\":\"138.134.15.25:25545\",\"connected\":true,\"resource\":100,\"totalResource\":200,\"network\":100,\"totalNetwork\":200,\"location\":77}");

        //Add 1 duplication, node list should not change much
        matchMaker.out("matchMaker register_node {\"id\":\"node3\",\"ipAddress\":\"138.134.15.25:25545\",\"connected\":true,\"resource\":100,\"totalResource\":200,\"network\":100,\"totalNetwork\":200,\"location\":77}");
        int sizeShouldBe3 = matchMaker.getNodeList().size();
        Assert.assertEquals(3, sizeShouldBe3);

        //Update here, node 3 resource chang from 100 -> 105
        matchMaker.out("matchMaker register_node {\"id\":\"node3\",\"ipAddress\":\"138.134.15.25:25545\",\"connected\":true,\"resource\":105,\"totalResource\":200,\"network\":100,\"totalNetwork\":200,\"location\":77}");
        EdgeNode node3 = matchMaker.getNodeList().get(2);
        //Node 3 resource should be 105
        long node3Resource = node3.getResource();
        Assert.assertEquals(200, node3Resource);
    }

    @Test
    public void registerAndUpdateClientTest() throws Exception {
        matchMaker.out("matchMaker register_client {\"id\":\"client1\",\"reqNetwork\":5,\"reqResource\":10,\"location\":54}");
        matchMaker.out("matchMaker register_client {\"id\":\"client2\",\"reqNetwork\":20,\"reqResource\":40,\"location\":42}");
        //Add 1 duplication, client list should not change much
        matchMaker.out("matchMaker register_client {\"id\":\"client2\",\"reqNetwork\":20,\"reqResource\":40,\"location\":42}");
        int sizeShouldBe2 = matchMaker.getClientList().size();
        Assert.assertEquals(2, sizeShouldBe2);

        //Update client
        matchMaker.out("matchMaker register_client {\"id\":\"client2\",\"reqNetwork\":21,\"reqResource\":40,\"location\":42}");
        //Size should still be the same
        Assert.assertEquals(2, sizeShouldBe2);
        //RegNetwork change from 20 -> 21
        Assert.assertEquals(21, matchMaker.getClientList().get(1).getReqNetwork());
    }

    @Test
    public void assignNodeToClientCompleteTest() throws Exception {
        //Register 3 node
        matchMaker.out("matchMaker register_node {\"id\":\"node1\",\"ipAddress\":\"68.131.232.215:30968\",\"connected\":true,\"resource\":100,\"totalResource\":200,\"network\":100,\"totalNetwork\":200,\"location\":55}");
        matchMaker.out("matchMaker register_node {\"id\":\"node2\",\"ipAddress\":\"92.183.84.109:42589\",\"connected\":true,\"resource\":100,\"totalResource\":200,\"network\":100,\"totalNetwork\":200,\"location\":33}");
        matchMaker.out("matchMaker register_node {\"id\":\"node3\",\"ipAddress\":\"138.134.15.25:25545\",\"connected\":true,\"resource\":100,\"totalResource\":200,\"network\":100,\"totalNetwork\":200,\"location\":77}");
        matchMaker.out("matchMaker register_client {\"id\":\"client1\",\"reqNetwork\":5,\"reqResource\":10,\"location\":54}");
        matchMaker.out("matchMaker register_client {\"id\":\"client2\",\"reqNetwork\":20,\"reqResource\":40,\"location\":42}");

        matchMaker.out("matchMaker assign_client client1");

        //client1 should be mapped to node1
        String thisShouldBeNode1 = matchMaker.getMapping().get("client1");
        Assert.assertEquals("node1", thisShouldBeNode1);

        //Check total node in list
        int sizeShouldBe3 = matchMaker.getNodeList().size();
        Assert.assertEquals(3, sizeShouldBe3);

        //job failed -> client 1 score with node 1 should be 10
        matchMaker.out("matchMaker disconnect_client {\"id\":\"client1\",\"message\":\"job_failed\"}");
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
        matchMaker.out("matchMaker assign_client client1");
        //2s passed, clint1-node1 score should be 8
        long thisShouldBe8  =  matchMaker.getClientList().get(0).getClientHistory().getHistoryScore("node1");
        Assert.assertEquals(8,thisShouldBe8);

        //Client 1 should connect to node 2 now cuz node 2 have better score
        String thisShouldBeNode2 = matchMaker.getMapping().get("client1");
        Assert.assertEquals("node2",thisShouldBeNode2);
    }

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();
}
