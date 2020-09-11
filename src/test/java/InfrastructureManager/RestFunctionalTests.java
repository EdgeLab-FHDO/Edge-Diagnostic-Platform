package InfrastructureManager;

import InfrastructureManager.Rest.RestInput;
import InfrastructureManager.Rest.RestOutput;
import InfrastructureManager.Rest.RestRunner;
import org.junit.*;

import io.restassured.RestAssured;
import io.restassured.path.json.config.JsonPathConfig;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.*;
import org.apache.http.HttpStatus;
import org.junit.*;

import java.lang.reflect.Array;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class RestFunctionalTests {
    private static RequestSpecification requestSpec;
    private static String testIp = "http://localhost";
    private static int port = 4567;
    RestOutput producer = new RestOutput();

    @BeforeClass
    public static void startServer() throws Exception {
        requestSpec = new RequestSpecBuilder().
                setBaseUri(testIp).
                setPort(port).
                build();

        RestRunner.getRestRunner().startServerIfNotRunning();
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
    public void testGetRequest() {
        String request = "test";
        String response =
                given().
                        spec(requestSpec).
                        pathParam("input", request).
                        when().
                        get("/client/test/{input}").
                        asString();
        Assert.assertEquals(request, response);
    }

    @Test
    public void commandSubmissionTest() {
        String request = "resttest";
        given().
            spec(requestSpec).
            pathParam("command", request).
        when().
            post("/node/execute/{command}").
        then().
            assertThat().
            statusCode(200);
    }
    /*
    @Test
    public void commandExecutionOutputTest() {
        String request = "rest_test";
        String expected = "test";
        String response = "";
        given().
            spec(requestSpec).
            pathParam("command", request).
            when().
            post("/node/execute/{command}");
        while(!RestOutput.outputIsAvailable()) { wait; }
        response = RestOutput.printResponse();
        Assert.assertEquals(expected,response);
    }
    */
}