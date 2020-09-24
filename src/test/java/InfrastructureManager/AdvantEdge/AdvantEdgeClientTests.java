package InfrastructureManager.AdvantEdge;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.File;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;


public class AdvantEdgeClientTests {
    private final AdvantEdgeClient client = new AdvantEdgeClient();
    private final String scenarioName = "dummy-test";
    private final int portNumber = 10500;

    @Rule //Mock server on port 80
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
    public void YAML2JSONParserTest() {
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
