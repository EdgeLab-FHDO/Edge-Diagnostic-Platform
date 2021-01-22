package InfrastructureManager.Modules.REST.OutputUnitTests;

import InfrastructureManager.Master;
import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleExecutionException;
import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleNotFoundException;
import InfrastructureManager.Modules.REST.Exception.Output.RESTOutputException;
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
    private final GETOutput get1 = new GETOutput("get1","/rest/get_test");
    private final ParametrizedGETOutput get2 = new ParametrizedGETOutput("get2",
            "/rest/get_test2/:id","id");

    @BeforeClass
    public static void setUpMasterAndStartServer() throws ModuleNotFoundException {
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
    public void GenericGETOutputTest() throws ModuleExecutionException {
        String path = "/rest/get_test";
        get1.write("toGET resource " + JSONExample);
        String responseBody = given().spec(requestSpec).get(path).asString();
        Assert.assertEquals(responseBody,JSONExample);
    }

    @Test
    public void ParametrizedGETOutputTest() throws ModuleExecutionException {
        String path = "/rest/get_test2/test_id";
        get2.write("toGET resource test_id " + JSONExample);
        String responseBody = given().spec(requestSpec).get(path).asString();
        Assert.assertEquals(JSONExample,responseBody);

    }

    @Test
    public void InGenericOutputIncompleteCommandThrowsException() {
        String command = "toGET resource"; //Missing the json body
        String expected = "Arguments missing for command " + command + " to REST Output get1";
        assertException(RESTOutputException.class,expected,() -> get1.write(command));
    }

    @Test
    public void InGenericOutputInvalidCommandThrowsException() {
        String command = "toGET notACommand";
        String expected = "Invalid command notACommand for REST output get1";
        assertException(RESTOutputException.class, expected, () -> get1.write(command));
    }

    @Test
    public void InParameterOutputIncompleteCommandThrowsException() {
        String command = "toGET resource"; //Missing the json body
        String expected = "Arguments missing for command " + command + " to REST Output get2";
        assertException(RESTOutputException.class,expected,() -> get2.write(command));
    }

    @Test
    public void InParameterOutputInvalidCommandThrowsException() {
        String command = "toGET notACommand";
        String expected = "Invalid command notACommand for REST output get2";
        assertException(RESTOutputException.class, expected, () -> get2.write(command));
    }

}
