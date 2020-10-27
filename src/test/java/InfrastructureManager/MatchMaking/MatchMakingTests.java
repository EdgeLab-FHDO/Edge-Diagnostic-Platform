package InfrastructureManager.MatchMaking;

import InfrastructureManager.EdgeNode;
import InfrastructureManager.Master;
import InfrastructureManager.Rest.RestOutput;
import InfrastructureManager.Rest.RestRunner;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static io.restassured.RestAssured.given;

public class MatchMakingTests {
    private final MatchMaker matchMaker = new MatchMaker();
    private static RequestSpecification requestSpec;

    @BeforeClass
    public static void startServer() throws Exception { //Before all tests
        String testIp = "http://localhost";
        int port = 4567;
        requestSpec = new RequestSpecBuilder().
                setBaseUri(testIp).
                setPort(port).
                build();
        RestRunner.getRestRunner("RestRunner", port).startServerIfNotRunning();
    }

    @Test
    public void registerNodeTest() {
        matchMaker.out("matchMaker register_node {\"id\":\"node1\",\"ipAddress\":\"192.168.0.1\",\"connected\":true}");
        String expected = Master.getInstance().getAvailableNodes().get(0).getId();
        Assert.assertEquals(expected, "node1");
    }

    @Test
    public void registerClientTest() throws Exception {
        Master.getInstance().addNode(new EdgeNode("node1","192.168.0.1",true));
        matchMaker.out("matchMaker register_client {\"id\":\"client1\"}");
        Assert.assertNotNull(Master.getInstance().getClientByID("client1"));
    }

    @Test
    public void matchMakerAsInputTest() throws Exception {
        Master.getInstance().addNode(new EdgeNode("node1","192.168.0.1",true));
        matchMaker.out("matchMaker register_client {\"id\":\"client1\"}");
        matchMaker.out("matchMaker assign_client client1");
        Assert.assertEquals(matchMaker.read(),"give_node client1");
    }

    @Test
    public void assignNodeToClientWithRestOutputTest() {
        Master.getInstance().addNode(new EdgeNode("node1","192.168.0.1",true));
        matchMaker.out("matchMaker register_client {\"id\":\"client1\"}");
        matchMaker.out("matchMaker assign_client client1");
        RestOutput.getInstance().out("restOut sendNode client1 node1");
        String expected = "{\"id\":\"node1\",\"ipAddress\":\"192.168.0.1\",\"connected\":true}";

        String response = given().spec(requestSpec)
                .when().get("/client/get_node/client1")
                .asString();
        Assert.assertEquals(expected, response);
    }

    @Test
    public void assignNodeToClientCompleteTest() throws Exception {
        Master.getInstance().addNode(new EdgeNode("node1","192.168.0.1",true));
        matchMaker.out("matchMaker register_client {\"id\":\"client1\"}");
        matchMaker.out("matchMaker assign_client client1 node1");
        RestOutput.getInstance().out(Master.getInstance().execute(matchMaker.read()));
        String expected = "{\"id\":\"node1\",\"ipAddress\":\"192.168.0.1\",\"connected\":true}";

        String response = given().spec(requestSpec)
                .when().get("/client/get_node/client1")
                .asString();
        Assert.assertEquals(expected, response);
    }

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void invalidCommandThrowsException() {
        exceptionRule.expect(IllegalArgumentException.class);
        exceptionRule.expectMessage("Invalid command for MatchMaker");
        matchMaker.out("matchMaker notACommand");
    }

    @Test
    public void incompleteCommandThrowsException() {
        exceptionRule.expect(IllegalArgumentException.class);
        exceptionRule.expectMessage("Arguments missing for command - MatchMaker");
        matchMaker.out("matchMaker register_client"); //Missing the client
    }

}
