package InfrastructureManager.Modules.REST.InputUnitTests;

import InfrastructureManager.Master;
import InfrastructureManager.Modules.REST.Input.POSTInput;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.AfterClass;
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
    public static void setUpMasterAndStartServer() {
        String testIp = "http://localhost";
        int port = 4567;
        requestSpec = new RequestSpecBuilder().
                setBaseUri(testIp).
                setPort(port).
                build();
        Master.changeConfigPath("src/test/resources/REST/RESTTestConfiguration.json");
        Master.resetInstance();
        Master.getInstance().startModule("rest");
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

    @AfterClass
    public static void stopServer() {
        Master.getInstance().exitAll();
    }
}
