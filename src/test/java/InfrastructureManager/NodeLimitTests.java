package InfrastructureManager;

import InfrastructureManager.Rest.RestOutput;
import InfrastructureManager.Rest.RestRunner;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.*;
import org.junit.rules.ExpectedException;

import static io.restassured.RestAssured.given;

public class NodeLimitTests {
    private RestOutput output = RestOutput.getInstance();
    private static RequestSpecification requestSpec;
    private static String testIp = "http://localhost";
    private static int port = 4567;

    @BeforeClass
    public static void startServer() throws Exception { //Before all tests
        requestSpec = new RequestSpecBuilder().
                setBaseUri(testIp).
                setPort(port).
                build();
        RestRunner.getRestRunner("RestRunner",port).startServerIfNotRunning();
    }

    @Before //Before each test flush the saved output
    public void resetOutput() {
        output.resetOutput();
    }

    @Test
    public void limitNodeWithDefaultPeriodTest() {
        output.out("restOut limit node1 1.5");
        String expected = "{\"node1\":\"150000_100000\"}";
        String response = given().spec(requestSpec)
                .when().get("/limit")
                .asString();
        Assert.assertEquals(expected, response);
    }

    @Test
    public void limitNodeWithCustomPeriodTest() {
        output.out("restOut limit node1 1.5 10000");
        String expected = "{\"node1\":\"15000_10000\"}";
        String response = given().spec(requestSpec)
                .when().get("/limit")
                .asString();
        Assert.assertEquals(expected, response);
    }

    @Test
    public void limitMoreThanOneNodeTest() {
        output.out("restOut limit node1 1.5");
        output.out("restOut limit node2 0.5");
        String expected = "{\"node1\":\"150000_100000\",\"node2\":\"50000_100000\"}";
        String response = given().spec(requestSpec)
                .when().get("/limit")
                .asString();
        Assert.assertEquals(expected, response);
    }

    @Test
    public void requestWithoutCommandFirstTest() {
        String expected = "{}";
        String response = given().spec(requestSpec)
                .when().get("/limit")
                .asString();
        Assert.assertEquals(expected, response);
    }

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void incompleteCommandThrowsException() {
        exceptionRule.expect(IllegalArgumentException.class);
        exceptionRule.expectMessage("Arguments missing for command - RESTOutput");
        output.out("restOut limit node1"); //Missing the cores
    }



}
