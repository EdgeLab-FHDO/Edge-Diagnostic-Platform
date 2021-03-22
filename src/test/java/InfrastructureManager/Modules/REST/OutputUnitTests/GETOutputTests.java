package InfrastructureManager.Modules.REST.OutputUnitTests;

import InfrastructureManager.Configuration.Exception.ConfigurationException;
import InfrastructureManager.Master;
import InfrastructureManager.ModuleManagement.Exception.Creation.ModuleManagerException;
import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleExecutionException;
import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleNotFoundException;
import InfrastructureManager.Modules.REST.Exception.Output.RESTOutputException;
import InfrastructureManager.Modules.REST.Output.GETOutput;
import InfrastructureManager.Modules.REST.Output.ParametrizedGETOutput;
import InfrastructureManager.Modules.REST.RESTModule;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import static InfrastructureManager.Modules.CommonTestingMethods.assertException;
import static io.restassured.RestAssured.given;

public class GETOutputTests {

    private static RequestSpecification requestSpec;
    public static final String JSONExample = "{\"name\":\"example\",\"number\":874}";
    private static RESTModule module;
    private final GETOutput get1 = getOutput("rest.get1");
    private final ParametrizedGETOutput get2 = (ParametrizedGETOutput) getOutput("rest.get2");

    private GETOutput getOutput(String name) {
        return module.getOutputs().stream()
                .map(o -> (GETOutput) o)
                .filter(o -> o.getName().equals(name))
                .findFirst()
                .orElseThrow();
    }

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
        module = (RESTModule) Master.getInstance().getManager().getModules().stream()
                .filter(m -> m.getName().equals("rest"))
                .findFirst()
                .orElseThrow();

    }

    @Test
    public void GenericGETOutputTest() throws ModuleExecutionException {
        String path = "/rest/get_test";
        get1.execute("toGET resource " + JSONExample);
        String responseBody = given().spec(requestSpec).get(path).asString();
        Assert.assertEquals(JSONExample, responseBody);
    }

    @Test
    public void beforeCreatingResourceValidPathReturnsEmptyString() throws RESTOutputException {
        GETOutput get3 = getOutput("rest.get3");
        String path = "/rest/get_test3";
        String expected1 = "";
        String responseBody = given().spec(requestSpec).get(path).asString();
        Assert.assertEquals(expected1, responseBody);
        get3.execute("toGET resource " + JSONExample);
        responseBody = given().spec(requestSpec).get(path).asString();
        Assert.assertEquals(JSONExample, responseBody);
    }

    @Test
    public void ParametrizedGETOutputTest() throws ModuleExecutionException {
        String path = "/rest/get_test2/test_id";
        get2.execute("toGET resource test_id " + JSONExample);
        String responseBody = given().spec(requestSpec).get(path).asString();
        Assert.assertEquals(JSONExample,responseBody);

    }

    @Test
    public void InGenericOutputIncompleteCommandThrowsException() {
        String command = "toGET resource"; //Missing the json body
        String expected = "Arguments missing for command " + command + " to REST Output rest.get1";
        assertException(RESTOutputException.class,expected,() -> get1.execute(command));
    }

    @Test
    public void InGenericOutputInvalidCommandThrowsException() {
        String command = "toGET notACommand";
        String expected = "Invalid command notACommand for REST output rest.get1";
        assertException(RESTOutputException.class, expected, () -> get1.execute(command));
    }

    @Test
    public void InParameterOutputIncompleteCommandThrowsException() {
        String command = "toGET resource"; //Missing the json body
        String expected = "Arguments missing for command " + command + " to REST Output rest.get2";
        assertException(RESTOutputException.class,expected,() -> get2.execute(command));
    }

    @Test
    public void InParameterOutputInvalidCommandThrowsException() {
        String command = "toGET notACommand";
        String expected = "Invalid command notACommand for REST output rest.get2";
        assertException(RESTOutputException.class, expected, () -> get2.execute(command));
    }

    @AfterClass
    public static void close() throws ModuleManagerException {
        Master.getInstance().getManager().exitAllModules();
        Master.resetInstance();
    }

}
