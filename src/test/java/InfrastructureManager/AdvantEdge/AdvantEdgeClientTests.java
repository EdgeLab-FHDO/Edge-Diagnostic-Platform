package InfrastructureManager.AdvantEdge;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;


public class AdvantEdgeClientTests {
    private final String scenarioName = "dummy-test";
    private final int portNumber = 10500;
    private final AdvantEdgeClient client = new AdvantEdgeClient(portNumber);

    @Rule //Mock server on port 10500
    public WireMockRule rule = new WireMockRule(options().port(portNumber),false);


    @Test
    public void addScenarioRequestTest() {
        String path = "/scenarios/" + scenarioName;
        String scenarioPath = "src/test/resources/AdvantEdge/dummy-test.json";
        client.out("advantEdge create " + scenarioName + " " + scenarioPath);
        verify(postRequestedFor(urlEqualTo(path))
                .withRequestBody(
                        matchingJsonPath("$.name", equalTo(scenarioName))
                ));
    }

    @Test // Checks if adding the scenario as a YAML file, sends a JSON in the request
    public void addYAMLScenarioRequestTest() {
        File convertedScenarioFile = new File ("src/test/resources/AdvantEdge/dummy-test-to-convert.json");

        String path = "/scenarios/" + scenarioName;
        String YAMLScenarioPath = "src/test/resources/AdvantEdge/dummy-test-to-convert.yaml";
        client.out("advantEdge create " + scenarioName + " " + YAMLScenarioPath);
        verify(postRequestedFor(urlEqualTo(path))
                .withRequestBody(
                        matchingJsonPath("$.name", equalTo(scenarioName))
                ));
        if (convertedScenarioFile.exists()) convertedScenarioFile.delete(); //Delete the generated JSON file, to be able to try again from scratch

    }

    @Test
    public void deployScenarioRequestTest() {
        String path = "/active/" + scenarioName;
        client.out("advantEdge deploy " + scenarioName);
        verify(postRequestedFor(urlEqualTo(path)));
    }

    @Test
    public void updateNetworkCharacteristics() throws IOException {
        String sandboxName = "test";
        String path = "/" + sandboxName + "/sandbox-ctrl/v1/events/NETWORK-CHARACTERISTICS-UPDATE";
        String jsonTestPath = "src/test/resources/AdvantEdge/network-update-test.json";
        client.out("advantEdge networkUpdate " + sandboxName + " fog-1 FOG 10 10 50 1 0");

        ObjectMapper mapper = new ObjectMapper();

        String jsonString = mapper.readValue(Paths.get(jsonTestPath).toFile(), String.class);

        verify(postRequestedFor(urlEqualTo(path))
                .withRequestBody(equalToJson(jsonString)));
    }

    @Test
    public void terminateScenarioRequestTest() {
        String path = "/active";
        client.out("advantEdge terminate");
        verify(deleteRequestedFor(urlEqualTo(path)));
    }

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void invalidCommandThrowsException() {
        exceptionRule.expect(IllegalArgumentException.class);
        exceptionRule.expectMessage("Invalid command for AdvantEdgeClient");
        client.out("advantEdge notACommand");
    }

    @Test
    public void incompleteCommandThrowsException() {
        exceptionRule.expect(IllegalArgumentException.class);
        exceptionRule.expectMessage("Arguments missing for command - AdvantEdgeClient");
        client.out("advantEdge create"); //Missing the name and path
    }
}
