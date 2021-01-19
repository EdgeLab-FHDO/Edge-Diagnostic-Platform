package InfrastructureManager.Modules.AdvantEDGE.OutputUnitTests;

import InfrastructureManager.Modules.AdvantEDGE.Exception.AdvantEdgeModuleException;
import InfrastructureManager.Modules.AdvantEDGE.Output.AdvantEdgeClient;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
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
    private final AdvantEdgeClient client = new AdvantEdgeClient("ae_client","http://localhost",portNumber);

    @Rule //Mock server on port 10500
    public WireMockRule rule = new WireMockRule(options().port(portNumber),false);


    @Test
    public void addScenarioRequestTest() throws AdvantEdgeModuleException {
        String path = "/platform-ctrl/v1/scenarios/" + scenarioName;
        String scenarioPath = "src/test/resources/AdvantEdge/dummy-test.json";
        client.out("advantEdge create " + scenarioName + " " + scenarioPath);
        verify(postRequestedFor(urlEqualTo(path))
                .withRequestBody(
                        matchingJsonPath("$.name", equalTo(scenarioName))
                ));
    }

    @Test // Checks if adding the scenario as a YAML file, sends a JSON in the request
    public void addYAMLScenarioRequestTest() throws AdvantEdgeModuleException {
        File convertedScenarioFile = new File ("src/test/resources/AdvantEdge/dummy-test-to-convert.json");

        String path = "/platform-ctrl/v1/scenarios/" + scenarioName;
        String YAMLScenarioPath = "src/test/resources/AdvantEdge/dummy-test-to-convert.yaml";
        client.out("advantEdge create " + scenarioName + " " + YAMLScenarioPath);
        verify(postRequestedFor(urlEqualTo(path))
                .withRequestBody(
                        matchingJsonPath("$.name", equalTo(scenarioName))
                ));
        if (convertedScenarioFile.exists()) convertedScenarioFile.delete(); //Delete the generated JSON file, to be able to try again from scratch

    }

    @Test
    public void deployScenarioRequestTest() throws IOException, AdvantEdgeModuleException {
        String path = "/platform-ctrl/v1/sandboxes/sandbox-" + scenarioName;
        String jsonTestPath = "src/test/resources/AdvantEdge/deploy-scenario.json";
        client.out("advantEdge deploy " + scenarioName);

        String jsonString = Files.readString(Paths.get(jsonTestPath), StandardCharsets.US_ASCII);

        verify(postRequestedFor(urlPathMatching(path))
                .withRequestBody(equalToJson(jsonString)));
    }

    @Test
    public void updateNetworkCharacteristics() throws IOException, AdvantEdgeModuleException {
        String sandboxName = "test";
        String path = "/" + sandboxName + "/sandbox-ctrl/v1/events/NETWORK-CHARACTERISTICS-UPDATE";
        String jsonTestPath = "src/test/resources/AdvantEdge/network-update-test.json";
        client.out("advantEdge networkUpdate " + sandboxName + " fog-1 FOG 10 10 50 1 0");

        String jsonString = Files.readString(Paths.get(jsonTestPath), StandardCharsets.US_ASCII);

        verify(postRequestedFor(urlPathMatching(path))
                .withRequestBody(equalToJson(jsonString)));
    }

    @Test
    public void terminateScenarioRequestTest() throws AdvantEdgeModuleException {
        String path = "/test_sandbox/sandbox-ctrl/v1/active/";
        client.out("advantEdge terminate test_sandbox");
        verify(deleteRequestedFor(urlEqualTo(path)));
    }

    @Test
    public void invalidCommandThrowsException() {
        assertException(IllegalArgumentException.class, "Invalid command for AdvantEdgeClient",() -> client.out("advantEdge notACommand"));
    }

    @Test
    public void incompleteCommandThrowsException() {
        assertException(IllegalArgumentException.class, "Arguments missing for command - AdvantEdgeClient", () ->client.out("advantEdge create"));
    }
}
