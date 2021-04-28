package InfrastructureManager.Modules.AdvantEDGE.OutputUnitTests;

import InfrastructureManager.Configuration.Exception.ConfigurationException;
import InfrastructureManager.Master;
import InfrastructureManager.ModuleManagement.Exception.Creation.ModuleManagerException;
import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleExecutionException;
import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleNotFoundException;
import InfrastructureManager.Modules.AdvantEDGE.AdvantEdgeModule;
import InfrastructureManager.Modules.AdvantEDGE.Exception.AdvantEdgeModuleException;
import InfrastructureManager.Modules.AdvantEDGE.Exception.ErrorInResponseException;
import InfrastructureManager.Modules.AdvantEDGE.Output.AdvantEdgeClient;
import InfrastructureManager.Modules.Scenario.ScenarioModule;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static InfrastructureManager.Modules.CommonTestingMethods.assertException;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;


public class AdvantEdgeClientTests {
    private final String scenarioName = "dummy-test";
    private final int portNumber = 10500;
    private static AdvantEdgeModule module ;
    private final AdvantEdgeClient client = new AdvantEdgeClient(module, "ae_client","http://localhost",portNumber);

    @BeforeClass
    public static void setUpMasterAndStartServer() throws ModuleNotFoundException, ConfigurationException, ModuleManagerException {
        Master.resetInstance();
        Master.getInstance().configure("src/test/resources/Modules/AdvantEDGE/AdvantEdgeModuleConfiguration.json");
        Master.getInstance().getManager().startModule("ae");
        module = (AdvantEdgeModule) Master.getInstance().getManager().getModules().stream()
                .filter(m -> m.getName().equals("ae"))
                .findFirst()
                .orElseThrow();

    }

    @Rule //Mock server on port 10500
    public WireMockRule rule = new WireMockRule(options().port(portNumber),false);


    @Test
    public void addScenarioRequestTest() throws ModuleExecutionException {
        String path = "/platform-ctrl/v1/scenarios/" + scenarioName;
        String scenarioPath = "src/test/resources/Modules/AdvantEDGE/dummy-test.json";
        try {
            client.execute("advantEdge create " + scenarioName + " " + scenarioPath);
        } catch (ErrorInResponseException ignored) {}
        verify(postRequestedFor(urlEqualTo(path))
                .withRequestBody(
                        matchingJsonPath("$.name", equalTo(scenarioName))
                ));
    }

    @Test // Checks if adding the scenario as a YAML file, sends a JSON in the request
    public void addYAMLScenarioRequestTest() throws ModuleExecutionException {
        File convertedScenarioFile = new File ("src/test/resources/AdvantEdge/dummy-test-to-convert.json");

        String path = "/platform-ctrl/v1/scenarios/" + scenarioName;
        String YAMLScenarioPath = "src/test/resources/Modules/AdvantEDGE/dummy-test-to-convert.yaml";
        try {
            client.execute("advantEdge create " + scenarioName + " " + YAMLScenarioPath);
        } catch (ErrorInResponseException ignored) {}

        verify(postRequestedFor(urlEqualTo(path))
                .withRequestBody(
                        matchingJsonPath("$.name", equalTo(scenarioName))
                ));
        if (convertedScenarioFile.exists()) {
            convertedScenarioFile.delete(); //Delete the generated JSON file, to be able to try again from scratch
        }

    }

    @Test
    public void deployScenarioRequestTest() throws IOException, ModuleExecutionException {
        String path = "/platform-ctrl/v1/sandboxes/sandbox-" + scenarioName;
        String jsonTestPath = "src/test/resources/Modules/AdvantEDGE/deploy-scenario.json";
        try {
            client.execute("advantEdge deploy " + scenarioName);
        } catch (ErrorInResponseException ignored) {}

        String jsonString = Files.readString(Paths.get(jsonTestPath), StandardCharsets.US_ASCII);

        verify(postRequestedFor(urlPathMatching(path))
                .withRequestBody(equalToJson(jsonString)));
    }

    @Test
    public void updateNetworkCharacteristics() throws IOException, ModuleExecutionException {
        String sandboxName = "test";
        String path = "/" + sandboxName + "/sandbox-ctrl/v1/events/NETWORK-CHARACTERISTICS-UPDATE";
        String jsonTestPath = "src/test/resources/Modules/AdvantEDGE/network-update-test.json";
        try {
            client.execute("advantEdge networkUpdate " + sandboxName + " fog-1 FOG 10 10 50 1 0");
        } catch (ErrorInResponseException ignored){}

        String jsonString = Files.readString(Paths.get(jsonTestPath), StandardCharsets.US_ASCII);

        verify(postRequestedFor(urlPathMatching(path))
                .withRequestBody(equalToJson(jsonString)));
    }

    @Test
    public void terminateScenarioRequestTest() throws ModuleExecutionException {
        String path = "/test_sandbox/sandbox-ctrl/v1/active/";
        try {
            client.execute("advantEdge terminate test_sandbox");
        } catch (ErrorInResponseException ignored){}
        verify(deleteRequestedFor(urlEqualTo(path)));
    }

    @Test
    public void invalidCommandThrowsException() {
        String command = "advantEdge notACommand";
        String expectedMessage = "Invalid command notACommand for AdvantEdgeClient";
        assertException(AdvantEdgeModuleException.class, expectedMessage,() -> client.execute(command));
    }

    @Test
    public void incompleteCommandThrowsException() {
        String command = "advantEdge create";
        String expectedMessage = "Arguments missing in command " + command + " to AdvantEdgeClient";
        assertException(AdvantEdgeModuleException.class, expectedMessage,() -> client.execute(command));
    }
}
