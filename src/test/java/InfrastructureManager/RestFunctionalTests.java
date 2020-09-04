package InfrastructureManager;

import InfrastructureManager.Rest.RestRouter;
import org.junit.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import io.restassured.RestAssured;
import io.restassured.path.json.config.JsonPathConfig;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.*;
import org.apache.http.HttpStatus;
import org.junit.*;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class RestFunctionalTests {
    private static RequestSpecification requestSpec;
    private static String testIp = "http://localhost";
    private static int port = 4567;

    @BeforeClass
    public static void startServer() {
        String [] args = {};
        requestSpec = new RequestSpecBuilder().
                setBaseUri(testIp).
                setPort(port).
                build();
        RestRouter.main(args);
    }

    @Test
    public void testReadCommand() {
        String command = "test";
        String response =
                given().
                    spec(requestSpec).
                    pathParam("command", command).
                when().
                    post("/node/command/{command}").
                    asString();
        Assert.assertEquals(command, response);
    }

    @Test
    public void testPrintResponse() {
        String request = "test";
        String response =
                given().
                        spec(requestSpec).
                        pathParam("request", request).
                        when().
                        get("/client/{request}").
                        asString();
        Assert.assertEquals(request, response);
    }
}