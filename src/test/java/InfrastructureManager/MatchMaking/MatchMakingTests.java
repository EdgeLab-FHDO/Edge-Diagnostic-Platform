package InfrastructureManager.MatchMaking;

import InfrastructureManager.Configuration.CommandSet;
import InfrastructureManager.Master;
import InfrastructureManager.REST.Output.ParametrizedGETOutput;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class MatchMakingTests {
    private final MatchMaker matchMaker = new MatchMaker("match_m","random");
    private final CommandSet commandSet= new CommandSet();
    private static RequestSpecification requestSpec;


    @BeforeClass
    public static void startServer() { //Before all tests
        String testIp = "http://localhost";
        int port = 4567;
        requestSpec = new RequestSpecBuilder().
                setBaseUri(testIp).
                setPort(port).
                build();
        Master.changeConfigPath("src/test/resources/MatchMaking/MatchMakingConfiguration.json");
        Master.getInstance().startRunnerThread("RestServer");
    }


    @Test
    public void registerNodeTest() {
        matchMaker.out("matchMaker register_node {\"id\":\"node1\",\"ipAddress\":\"192.168.0.1\",\"connected\":true}");
        String expected = matchMaker.getNodeList().get(0).getId();
        Assert.assertEquals(expected, "node1");
    }

    @Test
    public void registerClientTest() {
        matchMaker.out("matchMaker register_client {\"id\":\"client1\"}");
        String expected = matchMaker.getClientList().get(0).getId();
        Assert.assertEquals(expected, "client1");
    }

    @Test
    public void matchMakerAsInputTest() {
        String node = "{\"id\":\"node1\",\"ipAddress\":\"192.168.0.1\",\"connected\":true}";
        String expected = "give_node client1 " + node;
        matchMaker.out("matchMaker register_node {\"id\":\"node1\",\"ipAddress\":\"192.168.0.1\",\"connected\":true}");
        matchMaker.out("matchMaker register_client {\"id\":\"client1\"}");
        matchMaker.out("matchMaker assign_client client1");
        Assert.assertEquals(expected,matchMaker.read());
    }

    @Before
    public void setCommands() {
        Map<String,String> commands = new HashMap<>();
        commands.put("give_node $client_id $nodeJSON","toGET resource $client_id $nodeJSON");
        this.commandSet.set(commands);
    }

    @Test
    public void assignNodeToClientCompleteTest() {

        String path = "/client/get_node/:client_id";
        ParametrizedGETOutput output = new ParametrizedGETOutput("rest_out",path, "client_id");
        matchMaker.out("matchMaker register_node {\"id\":\"node1\",\"ipAddress\":\"192.168.0.1\",\"connected\":true}");
        matchMaker.out("matchMaker register_client {\"id\":\"client1\"}");
        matchMaker.out("matchMaker assign_client client1");
        output.out(Master.getInstance().execute(matchMaker.read(),commandSet));
        String expected = "{\"id\":\"node1\",\"ipAddress\":\"192.168.0.1\",\"connected\":true}";

        String response = given().spec(requestSpec)
                .when().get("/client/get_node/client1")
                .asString();
        Assert.assertEquals(expected, response);
    }

    @Test
    public void invalidCommandThrowsException() {
        var e = Assert.assertThrows(IllegalArgumentException.class, () ->
                matchMaker.out("matchMaker notACommand"));
        Assert.assertEquals(e.getMessage(),"Invalid command for MatchMaker");
    }

    @Test
    public void incompleteCommandThrowsException() {
        var e = Assert.assertThrows(IllegalArgumentException.class, () ->
                matchMaker.out("matchMaker register_client"));
        Assert.assertEquals(e.getMessage(),"Arguments missing for command - MatchMaker");
    }

}
