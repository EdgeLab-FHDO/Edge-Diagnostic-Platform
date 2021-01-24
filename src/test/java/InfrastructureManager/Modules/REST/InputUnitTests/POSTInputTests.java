package InfrastructureManager.Modules.REST.InputUnitTests;

import InfrastructureManager.Configuration.Exception.ConfigurationException;
import InfrastructureManager.Master;
import InfrastructureManager.ModuleManagement.Exception.Creation.ModuleManagerException;
import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleNotFoundException;
import InfrastructureManager.Modules.REST.Input.POSTInput;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static io.restassured.RestAssured.given;

public class POSTInputTests {

    private static RequestSpecification requestSpec;
    public static final String JSONExample = "{\"name\":\"example\",\"number\":874}";

    @BeforeClass
    public static void setUpMasterAndStartServer() throws ModuleNotFoundException, ConfigurationException, ModuleManagerException {
        String testIp = "http://localhost";
        int port = 4567;
        requestSpec = new RequestSpecBuilder().
                setBaseUri(testIp).
                setPort(port).
                build();
        Master.resetInstance();
        Master.getInstance().configure("src/test/resources/Modules/REST/RESTTestConfiguration.json");
        Master.getInstance().getManager().startModule("rest");
    }

    @Test
    public void POSTInputWithBodyInCommandTest() throws InterruptedException {
        String path ="/rest/post_test1";
        POSTInput in = new POSTInput("post1",path,"test $body", Collections.emptyList());
        String expected = "test " + JSONExample;
        Thread t = new Thread(()-> {
            given().spec(requestSpec).body(JSONExample).post(path);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        t.start();
        Assert.assertEquals(expected, in.read());
    }

    @Test
    public void POSTInputWithParametersInCommandTest() throws InterruptedException {
        String path = "/rest/post_test12";
        POSTInput in2 = new POSTInput("post2", path, "test2 $name $number", List.of("name","number"));
        String expected = "test2 example 874";
        Thread t = new Thread(()-> {
            try {
                given().spec(requestSpec).body(JSONExample).post(path);
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        t.start();
        Assert.assertEquals(expected, in2.read());
    }

    @Test
    public void POSTInputWithUndefinedParametersToMapThrowsExceptionAndReturnsItInResponse() {
        String path = "/rest/post_test13";
        POSTInput in3 = new POSTInput("post3", path, "test3 $name $number", List.of("name"));
        //Number was not included in the list
        String expected = "Argument number was not defined to be parsed";
        Thread thread = new Thread(() -> {
            try {
                in3.read();
            } catch (InterruptedException ignored) {}
        });
        thread.start(); //To start the new ROUTE, this thread will just be blocked
        Response response = given().spec(requestSpec).body(JSONExample).post(path);
        Assert.assertEquals(500, response.getStatusCode());
        Assert.assertEquals(expected, response.asString());
        thread.interrupt();
    }

    @Test
    public void POSTInputWithUnsupportedJSONParameterTypesThrowsExceptionAndReturnsItInResponse() {
        String path = "/rest/post_test14";
        POSTInput in4 = new POSTInput("post4", path, "test4 $name $array", List.of("name", "array"));
        String JSONExampleWithArray = "{\"name\":\"example\",\"array\":[1,2,3]}";
        String expected = "Parameter is not a String, Boolean or Number value";
        Thread thread = new Thread(() -> {
            try {
                in4.read();
            } catch (InterruptedException ignored) {}
        });
        thread.start(); //To start the new ROUTE, this thread will just be blocked
        Response response = given().spec(requestSpec).body(JSONExampleWithArray).post(path);
        Assert.assertEquals(500, response.getStatusCode());
        Assert.assertEquals(expected, response.asString());
        thread.interrupt();
    }
}
