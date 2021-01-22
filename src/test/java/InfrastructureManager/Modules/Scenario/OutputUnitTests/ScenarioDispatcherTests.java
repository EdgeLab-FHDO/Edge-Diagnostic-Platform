package InfrastructureManager.Modules.Scenario.OutputUnitTests;

import InfrastructureManager.Master;
import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleExecutionException;
import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleNotFoundException;
import InfrastructureManager.Modules.CommonTestingMethods;
import InfrastructureManager.Modules.Scenario.Event;
import InfrastructureManager.Modules.Scenario.Exception.Output.ScenarioDispatcherException;
import InfrastructureManager.Modules.Scenario.Scenario;
import InfrastructureManager.Modules.Scenario.ScenarioDispatcher;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.stream.Collectors;


public class ScenarioDispatcherTests {

    private final ScenarioDispatcher dispatcher;
    static final long WAITING_TIME = 12000; //If events are delayed this has to be modified
    private final ByteArrayOutputStream outContent;

    public ScenarioDispatcherTests() throws IOException {
        String scenarioPath = "src/test/resources/Modules/Scenario/dummyScenario.json";
        ObjectMapper mapper = new ObjectMapper();
        Scenario scenario = mapper.readValue(new File(scenarioPath), Scenario.class);
        dispatcher = new ScenarioDispatcher("dummy.dispatcher", scenario);
        outContent = new ByteArrayOutputStream();
    }

    @BeforeClass
    public static void configureMaster() {
        Master.changeConfigPath("src/test/resources/Modules/Scenario/ScenarioConfiguration.json");
        Master.resetInstance();
        Master.getInstance().startAllModules();
    }

    @Test
    public void scenarioIsCorrectlyLoaded() {
        String expected = "deploy_application" + "node_request" + "update_gui";
        String result = dispatcher.getScenario().getEventList()
                .stream()
                .map(Event::getCommand)
                .collect(Collectors.joining());
        Assert.assertEquals(expected, result);
    }

    @Test
    public void invalidCommandThrowsException() {
        String command = "dispatcher notACommand";
        String expected = "Invalid command notACommand for ScenarioDispatcher";
        assertExceptionInOutput(dispatcher,ScenarioDispatcherException.class, expected, command);
    }

    @Test
    public void wrongRunWithDelayCommandThrowsException() {
        String command = "dispatcher run -d wrongCommand";
        String expected = "Invalid run command wrongCommand";
        assertExceptionInOutput(dispatcher,ScenarioDispatcherException.class, expected, command);
    }
    @Test
    public void incompleteRunWithDelayCommandThrowsException() {
        String command = "dispatcher run -d";
        String expected = "Arguments missing for command " + command + " to ScenarioDispatcher";
        assertExceptionInOutput(dispatcher,ScenarioDispatcherException.class, expected, command);
    }

    @Test
    public void runningAScenarioNotRelatedToAModuleThrowsException() {
        Scenario otherScenario = new Scenario("aName.scenario");
        ScenarioDispatcher otherDispatcher = new ScenarioDispatcher("aName.dispatcher", otherScenario);
        String expected = "Module aName was not found";
        assertExceptionInOutput(otherDispatcher,ModuleNotFoundException.class,expected,"dispatcher run");
    }

    @Test
    public void pausingAScenarioNotRelatedToAModuleThrowsException() {
        Scenario otherScenario = new Scenario("aName.scenario");
        ScenarioDispatcher otherDispatcher = new ScenarioDispatcher("aName.dispatcher", otherScenario);
        String expected = "Module aName was not found";
        assertExceptionInOutput(otherDispatcher,ModuleNotFoundException.class,expected,"dispatcher pause");

    }
    @Test
    public void resumingAScenarioNotRelatedToAModuleThrowsException() {
        Scenario otherScenario = new Scenario("aName.scenario");
        ScenarioDispatcher otherDispatcher = new ScenarioDispatcher("aName.dispatcher", otherScenario);
        String expected = "Module aName was not found";
        assertExceptionInOutput(otherDispatcher,ModuleNotFoundException.class,expected,"dispatcher resume");

    }
    @Test
    public void stoppingAScenarioNotRelatedToAModuleThrowsException() {
        Scenario otherScenario = new Scenario("aName.scenario");
        ScenarioDispatcher otherDispatcher = new ScenarioDispatcher("aName.dispatcher", otherScenario);
        String expected = "Module aName was not found";
        assertExceptionInOutput(otherDispatcher,ModuleNotFoundException.class,expected,"dispatcher stop");

    }


    public void assertExceptionInOutput(ScenarioDispatcher output,Class<? extends  Throwable> exceptionClass, String expectedMessage, String command) {
        CommonTestingMethods.assertException(exceptionClass, expectedMessage, () -> output.write(command));
    }

    @Before //For testing the Standard Output
    public void captureStream() {
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void restoreStream() {
        System.setOut(System.out);
    }

    public static void waitFor(long waitingTime) {
        long start = System.currentTimeMillis();
        while (true) {
            if (System.currentTimeMillis() - start >= waitingTime) {
                return;
            }
        }
    }

    @Test
    public void runScenarioTest() throws ModuleExecutionException {

        String expected = "helmChartExecution\n" + "matchMakingExecution\n" +
                "GUIUpdateExecution\n";
        dispatcher.write("dispatcher run");
        //Waiting for all the events to be executed in time
        waitFor(WAITING_TIME);
        Assert.assertEquals(expected,outContent.toString().replaceAll("\r",""));

    }

    @Test
    public void runScenarioRelativeDelayTest() throws ModuleExecutionException {
        String expected = "helmChartExecution\n" + "matchMakingExecution\n" +
                "GUIUpdateExecution\n";
        int delay = 2000; //2 seconds delay
        dispatcher.write("dispatcher run -d -r " + delay);
        //Waiting for all the events to be executed in time
        waitFor(WAITING_TIME + delay);
        Assert.assertEquals(expected,outContent.toString().replaceAll("\r",""));

    }
}
