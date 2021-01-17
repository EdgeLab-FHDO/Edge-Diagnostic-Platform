package InfrastructureManager.Modules.REST.OutputUnitTests;

import InfrastructureManager.Master;
import InfrastructureManager.Modules.REST.Output.GETOutput;
import InfrastructureManager.Modules.REST.Output.ParametrizedGETOutput;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import static InfrastructureManager.Modules.CommonTestingMethods.assertException;
import static io.restassured.RestAssured.given;

public class GETOutputTests {

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
    public void GenericGETOutputTest() {
        String path = "/rest/get_test";
        GETOutput out = new GETOutput("get1",path);
        out.out("toGET resource " + JSONExample);
        String responseBody = given().spec(requestSpec).get(path).asString();
        Assert.assertEquals(responseBody,JSONExample);
    }

    @Test
    public void ParametrizedGETOutputTest(){
        String path = "/rest/get_test2/:id";
        ParametrizedGETOutput out = new ParametrizedGETOutput("get2",path,"id");
        out.out("toGET resource test_id " + JSONExample);
        String responseBody = given().spec(requestSpec).
                get("/rest/get_test2/test_id").asString();
        Assert.assertEquals(JSONExample,responseBody);

    }

    @Test
    public void InOutputIncompleteCommandThrowsException() {
        GETOutput out = new GETOutput("rest_out","/rest/get_test");
        String expected = "Arguments missing for command - REST";
        String command = "toGET resource"; //Missing the json body
        assertException(IllegalArgumentException.class,expected,() -> out.out(command));
    }

    @Test
    public void InOutputInvalidCommandThrowsException() {
        GETOutput out = new GETOutput("rest_out","/rest/get_test");
        String expected = "Invalid command for REST";
        String command = "toGET notACommand";
        assertException(IllegalArgumentException.class, expected, () -> out.out(command));
    }

}
