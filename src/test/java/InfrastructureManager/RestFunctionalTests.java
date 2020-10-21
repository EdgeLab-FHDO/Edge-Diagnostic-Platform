package InfrastructureManager;

import InfrastructureManager.Rest.RestRunner;
import org.junit.*;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.*;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class RestFunctionalTests {
    private static RequestSpecification requestSpec;
    private static String testIp = "http://localhost";
    private static int port = 4567;

    @BeforeClass
    public static void startServer() throws Exception {
        requestSpec = new RequestSpecBuilder().
                setBaseUri(testIp).
                setPort(port).
                build();

        RestRunner.getRestRunner("restRunner",port).startServerIfNotRunning();
    }

    @Test
    public void testPostRequest() {
        String command = "test";
        String response =
                given().
                    spec(requestSpec).
                    pathParam("input", command).
                when().
                    post("/node/test/read/{input}").
                    asString();
        Assert.assertEquals(command, response);
    }

    @Test
    public void postSubmissionTest() {
        String request = "resttest";
        String response =
            given().
                spec(requestSpec).
                pathParam("command", request).
            when().
                post("/node/execute/{command}").
                asString();
        Assert.assertEquals("200", response);
    }

    @Test
    public void getNodeTest() {
        given().
            spec(requestSpec).
        when().
            post("/client/get_node").
        then().
            assertThat().
            body("content", notNullValue());
    }
}