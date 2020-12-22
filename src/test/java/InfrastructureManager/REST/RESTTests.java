package InfrastructureManager.REST;

import InfrastructureManager.Master;
import InfrastructureManager.MasterOutput;
import InfrastructureManager.REST.Input.POSTInput;
import InfrastructureManager.REST.Output.GETOutput;
import InfrastructureManager.REST.Output.ParametrizedGETOutput;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static io.restassured.RestAssured.given;

public class RESTTests {

    private static RequestSpecification requestSpec;
    public static final String JSONExample = "{\"name\":\"example\",\"number\":874}";

    @BeforeClass
    public static void setUpMasterAndStartServer() {
        String testIp = "http://localhost";
        int port = 4567;
        requestSpec = new RequestSpecBuilder().
                setBaseUri(testIp).
                setPort(port).
                build();
        Master.changeConfigPath("src/test/resources/REST/RESTTestConfiguration.json");
        Master.resetInstance();
        Master.getInstance().startRunnerThread("RestServer");
    }

    @Test
    public void POSTInputWithBodyInCommandTest() {
        String path ="/post_test1";
        POSTInput in = new POSTInput(path,"test1 $body", Collections.emptyList());
        String expected = "test1 " + JSONExample;
        Thread t = new Thread(()-> {
            given().spec(requestSpec).body(JSONExample).post(path);
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        t.start();
        Assert.assertEquals(expected, in.read());
    }

    @Test
    public void POSTInputWithParametersInCommandTest() {
        String path = "/post_test2";
        POSTInput in2 = new POSTInput(path, "test2 $name $number", List.of("name","number"));
        String expected = "test2 example 874";
        Thread t = new Thread(()-> {
            given().spec(requestSpec).body(JSONExample).post(path);
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        t.start();
        Assert.assertEquals(expected, in2.read());
    }

    @Test
    public void GenericGETOutputTest() {
        String path = "/get_test1";
        GETOutput out = new GETOutput("get1",path);
        out.out("toGET resource " + JSONExample);
        String responseBody = given().spec(requestSpec).get(path).asString();
        Assert.assertEquals(responseBody,JSONExample);
    }

    @Test
    public void ParametrizedGETOutputTest(){
        String path = "/get_test2/:id";
        ParametrizedGETOutput out = new ParametrizedGETOutput("get2",path,"id");
        out.out("toGET resource test_id " + JSONExample);
        String responseBody = given().spec(requestSpec).
                get("/get_test2/test_id").asString();
        Assert.assertEquals(JSONExample,responseBody);

    }

    @Test
    public void InOutputIncompleteCommandThrowsException() {
        GETOutput out = new GETOutput("rest_out","/get_test1");
        String expected = "Arguments missing for command - REST";
        String command = "toGET resource"; //Missing the json body
        assertException(IllegalArgumentException.class,out,command,expected);
    }

    @Test
    public void InOutputInvalidCommandThrowsException() {
        GETOutput out = new GETOutput("rest_out","/get_test1");
        String expected = "Invalid command for REST";
        String command = "toGET notACommand";
        assertException(IllegalArgumentException.class,out,command,expected);
    }

    public void assertException(Class<? extends  Throwable> exceptionClass, MasterOutput output, String command , String expectedMessage) {
        var e = Assert.assertThrows(exceptionClass, () -> output.out(command));
        Assert.assertEquals(expectedMessage, e.getMessage());
    }
}
