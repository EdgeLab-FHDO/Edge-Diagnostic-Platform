package InfrastructureManager.MatchMaking;

import InfrastructureManager.Configuration.CommandSet;
import InfrastructureManager.EdgeClient;
import InfrastructureManager.EdgeNode;
import InfrastructureManager.Master;
import InfrastructureManager.Rest.RestOutput;
import InfrastructureManager.Rest.RestRunner;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.*;
import org.junit.rules.ExpectedException;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class MatchMakingTests {
    private final MatchMaker matchMaker = new MatchMaker("match_m","random");
    private final CommandSet commandSet= new CommandSet();
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
    public void matchMakerAsInputTest() throws Exception {
        matchMaker.out("matchMaker register_node {\"id\":\"node1\",\"ipAddress\":\"192.168.0.1\",\"connected\":true}");
        matchMaker.out("matchMaker register_client {\"id\":\"client1\"}");
        matchMaker.out("matchMaker assign_client client1");
        Assert.assertEquals(matchMaker.read(),"give_node client1 node1");
    }

    @Before
    public void fillCommands() {
        Map<String,String> commands = new HashMap<>();
        commands.put("give_node $client_id $node_id","restOut sendNode $client_id $node_id");
        this.commandSet.set(commands);
    }

    @Test
    public void assignNodeToClientCompleteTest() throws Exception {


        Master.getInstance().addClient(new EdgeClient("client1"));
        Master.getInstance().addNode(new EdgeNode("node1","192.168.0.1",true, 10200));
        matchMaker.out("matchMaker register_node {\"id\":\"node1\",\"ipAddress\":\"192.168.0.1\",\"connected\":true,\"port\":10200}");
        matchMaker.out("matchMaker register_client {\"id\":\"client1\"}");
        matchMaker.out("matchMaker assign_client client1");
        RestOutput.getInstance().out(Master.getInstance().execute(matchMaker.read(),commandSet));
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
