/*
package InfrastructureManager;

import org.junit.*;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.*;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

//TODO: Delete
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

        RestRunner.getRestRunner("RestRunner",port).startServerIfNotRunning();
    }


    @Test
    public void postSubmissionTest() {
        String response =
            given().
                spec(requestSpec)
                    .contentType("application/json")
                    .body("{\"id\":\"client1\"}").
            when().
                post("/client/register").
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
}*/