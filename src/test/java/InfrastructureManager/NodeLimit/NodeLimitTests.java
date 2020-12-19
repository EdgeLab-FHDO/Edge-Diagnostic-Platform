package InfrastructureManager.NodeLimit;

import InfrastructureManager.Master;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class NodeLimitTests {

    private static final NodeResourceLimiter limiter = new NodeResourceLimiter("limit");

    private static RequestSpecification requestSpec;

    @BeforeClass
    public static void startServer() { //Before all tests
        String testIp = "http://localhost";
        int port = 4567;
        requestSpec = new RequestSpecBuilder().
                setBaseUri(testIp).
                setPort(port).
                build();
        Master.changeConfigPath("src/test/resources/NodeLimit/NodeLimitConfiguration.json");
        Master.getInstance().startRunnerThread("RestServer");
    }

    @Before //Before each test flush the saved output
    public void resetOutput() {
        limiter.resetOutput();
    }

    @Test
    public void limitNodeWithDefaultPeriodTest() throws Exception {
        limiter.out("limit cores node1 1.5");
        String expected = "set_limits {\"node1\":\"150000_100000\"}";
        Assert.assertEquals(expected,limiter.read());
    }


    @Test
    public void limitNodeWithCustomPeriodTest() throws Exception {
        limiter.out("limit cores node1 1.5 10000");
        String expected = "set_limits {\"node1\":\"15000_10000\"}";
        Assert.assertEquals(expected, limiter.read());
    }


    @Test
    public void limitMoreThanOneNodeTest() throws Exception {
        limiter.out("limit cores node1 1.5");
        limiter.out("limit cores node2 0.5");
        String expected = "set_limits {\"node1\":\"150000_100000\",\"node2\":\"50000_100000\"}";
        Assert.assertEquals(expected, limiter.read());
    }

    @Test
    public void requestWithoutCommandFirstTest() {
        int response = given().spec(requestSpec)
                .when().get("/node/limit").getStatusCode();
        Assert.assertEquals(404, response);
    }

    @Test
    public void incompleteCommandThrowsException() {
        var e = Assert.assertThrows(IllegalArgumentException.class, () ->
                limiter.out("limit cores"));
        Assert.assertEquals(e.getMessage(),"Arguments missing for command - NodeLimiter");
    }

    @Test
    public void invalidCommandThrowsException() {
        var e = Assert.assertThrows(IllegalArgumentException.class, () ->
                limiter.out("limit notACommand"));
        Assert.assertEquals(e.getMessage(),"Invalid command for NodeLimiter");
    }

}
