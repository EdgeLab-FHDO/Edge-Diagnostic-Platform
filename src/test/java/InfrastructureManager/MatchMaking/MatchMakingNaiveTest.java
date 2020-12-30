package InfrastructureManager.MatchMaking;

import InfrastructureManager.Configuration.CommandSet;
import InfrastructureManager.Master;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class MatchMakingNaiveTest {

    private final MatchMaker matchMaker = new MatchMaker("match_m", "naive");
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

        //Disconnect node1, ready for re assign
        matchMaker.out("matchMaker disconnect_client {\"id\":\"client1\",\"message\":\"job_failed\"}");

        //Re assign client 1
        matchMaker.out("matchMaker assign_client client1");
        //Should still be node 1, the distance haven't change yet
        String thisShouldBeNode1Again = matchMaker.getMapping().get("client1");
        Assert.assertEquals("node1", thisShouldBeNode1Again);

        //Assign client 2
        matchMaker.out("matchMaker assign_client client2");
        String thisShouldBeNode2 = matchMaker.getMapping().get("client2");
        //Should be node 2 because node2 is closer to client 2 than others
        Assert.assertEquals("node2", thisShouldBeNode2);

        //Disconnect client 2
        matchMaker.out("matchMaker disconnect_client {\"id\":\"client2\",\"message\":\"job_failed\"}");
        //update node2 so it will be damn far away from client 2, making client 2 must connect to node 1
        matchMaker.out("matchMaker register_node {\"id\":\"node2\",\"ipAddress\":\"92.183.84.109:42589\",\"connected\":true,\"resource\":100,\"totalResource\":200,\"network\":100,\"totalNetwork\":200,\"location\":133}");
        matchMaker.out("matchMaker assign_client client2");
        String thisShouldBeNode1ForThis = matchMaker.getMapping().get("client2");
        //Should be node 1 because node1 is closer to client 2 than others
        Assert.assertEquals("node1", thisShouldBeNode1ForThis);

    }

}

