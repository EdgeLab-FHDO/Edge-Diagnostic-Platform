package InfrastructureManager.Modules.Scenario.InputUnitTests;

import InfrastructureManager.Configuration.Exception.ConfigurationException;
import InfrastructureManager.Master;
import InfrastructureManager.ModuleManagement.Exception.Creation.ModuleManagerException;
import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleNotFoundException;
import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;
import InfrastructureManager.Modules.CommonTestingMethods;
import InfrastructureManager.Modules.Scenario.Event;
import InfrastructureManager.Modules.Scenario.Exception.Input.InvalidTimeException;
import InfrastructureManager.Modules.Scenario.Exception.Input.OwnerModuleNotSetUpException;
import InfrastructureManager.Modules.Scenario.Scenario;
import InfrastructureManager.Modules.Scenario.ScenarioModule;
import InfrastructureManager.Modules.Utility.UtilityModule;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.stream.Collectors;

public class ScenarioInputTests {

    private final Scenario scenario;
    private static ScenarioModule module;

    @BeforeClass
    public static void setUpMasterAndStartServer() throws ModuleNotFoundException, ConfigurationException, ModuleManagerException {
        Master.resetInstance();
        Master.getInstance().configure("src/test/resources/Modules/Scenario/ScenarioConfiguration.json");
        Master.getInstance().getManager().startModule("dummy");
        module = (ScenarioModule) Master.getInstance().getManager().getModules().stream()
                .filter(m -> m.getName().equals("dummy"))
                .findFirst()
                .orElseThrow();
    }

    public ScenarioInputTests() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String scenarioPath = "src/test/resources/Modules/Scenario/dummyScenario.json";
        //ScenarioModule module =new ScenarioModule();
        InjectableValues inject = new InjectableValues.Std().addValue(ImmutablePlatformModule.class, module);
        mapper.setInjectableValues(inject);
        scenario = mapper.readValue(new File(scenarioPath),Scenario.class);
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
