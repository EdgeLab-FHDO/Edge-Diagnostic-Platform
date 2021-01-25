package InfrastructureManager.Modules.Scenario.InputUnitTests;

import InfrastructureManager.Modules.CommonTestingMethods;
import InfrastructureManager.Modules.Scenario.Event;
import InfrastructureManager.Modules.Scenario.Exception.Input.InvalidTimeException;
import InfrastructureManager.Modules.Scenario.Exception.Input.OwnerModuleNotSetUpException;
import InfrastructureManager.Modules.Scenario.Scenario;
import InfrastructureManager.Modules.Scenario.ScenarioModule;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.stream.Collectors;

public class ScenarioInputTests {

    private final Scenario scenario;

    public ScenarioInputTests() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String scenarioPath = "src/test/resources/Modules/Scenario/dummyScenario.json";
        ScenarioModule module =new ScenarioModule();
        scenario = mapper.readValue(new File(scenarioPath),Scenario.class);
        scenario.setOwnerModule(module);
    }

    @Test
    public void blocksIfNotStarted() throws InterruptedException {
        Thread inputRunner = new Thread(() -> {
            try {
                scenario.read();
            } catch (InterruptedException ignored) {}
        });
        inputRunner.start();
        Thread.sleep(5000); //Wait 5 seconds, and then check thread is still blocked
        Assert.assertEquals(Thread.State.WAITING, inputRunner.getState());
        inputRunner.interrupt();
    }

    @Test
    public void startTimeInThePastThrowsException() {
        String expected = "Start time in the past!";
        CommonTestingMethods.assertException(InvalidTimeException.class, expected, () -> scenario.setStartTime(0));

    }

    @Test
    public void commandsAreSentCorrectly() throws InterruptedException, InvalidTimeException, OwnerModuleNotSetUpException {
        int events = scenario.getEventList().size();
        StringBuilder result = new StringBuilder();
        scenario.setStartTime(System.currentTimeMillis() + 1000);
        scenario.start();
        for (int i = 0; i < events; i++) {
            result.append(scenario.read());
        }
        String expected = getConcatenatedEventCommandsInScenario();
        Assert.assertEquals(expected, result.toString());
    }

    private String getConcatenatedEventCommandsInScenario() {
        return scenario.getEventList()
                .stream()
                .map(Event::getCommand)
                .collect(Collectors.joining());
    }

}
