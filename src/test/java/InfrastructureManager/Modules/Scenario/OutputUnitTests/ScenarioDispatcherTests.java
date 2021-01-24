package InfrastructureManager.Modules.Scenario.OutputUnitTests;

import InfrastructureManager.Configuration.Exception.ConfigurationException;
import InfrastructureManager.Master;
import InfrastructureManager.ModuleManagement.Exception.Creation.ModuleManagerException;
import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleExecutionException;
import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleNotFoundException;
import InfrastructureManager.ModuleManagement.ModuleManager;
import InfrastructureManager.Modules.CommonTestingMethods;
import InfrastructureManager.Modules.Scenario.Event;
import InfrastructureManager.Modules.Scenario.Exception.Output.ScenarioDispatcherException;
import InfrastructureManager.Modules.Scenario.Scenario;
import InfrastructureManager.Modules.Scenario.ScenarioDispatcher;
import InfrastructureManager.Modules.Scenario.ScenarioModule;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.stream.Collectors;


public class ScenarioDispatcherTests {

    private ScenarioDispatcher dispatcher;
    private final Scenario scenario;
    static final long WAITING_TIME = 12000; //If events are delayed this has to be modified
    private final ByteArrayOutputStream outContent;
    private static ScenarioModule module = new ScenarioModule();


    public ScenarioDispatcherTests() throws IOException {
        String scenarioPath = "src/test/resources/Modules/Scenario/dummyScenario.json";
        ObjectMapper mapper = new ObjectMapper();
        this.scenario = mapper.readValue(new File(scenarioPath), Scenario.class);
        outContent = new ByteArrayOutputStream();
    }

    @BeforeClass
    public static void setUp() throws ConfigurationException, ModuleManagerException, ModuleNotFoundException {
        Master.resetInstance();
        Master.getInstance().configure("src/test/resources/Modules/Scenario/ScenarioConfiguration.json");
        ModuleManager manager = Master.getInstance().getManager();
        manager.startAllModules();
        module = findModule(manager);
    }

    @Before
    public void init() {
        dispatcher = new ScenarioDispatcher(module,"dummy.dispatcher", scenario);
    }

    private static ScenarioModule findModule(ModuleManager manager) throws ModuleNotFoundException, ModuleManagerException {
        return (ScenarioModule) manager.getModules().stream()
                .filter(m -> m.getName().equals("dummy"))
                .findFirst()
                .orElseThrow(() -> new ModuleNotFoundException("There is no module for dummy scenario"));
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

    public void assertExceptionInOutput(ScenarioDispatcher output,Class<? extends  Throwable> exceptionClass, String expectedMessage, String command) {
        CommonTestingMethods.assertException(exceptionClass, expectedMessage, () -> output.execute(command));
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
        dispatcher.execute("dispatcher run");
        //Waiting for all the events to be executed in time
        waitFor(WAITING_TIME);
        Assert.assertEquals(expected,outContent.toString().replaceAll("\r",""));

    }

    @Test
    public void runScenarioRelativeDelayTest() throws ModuleExecutionException {
        String expected = "helmChartExecution\n" + "matchMakingExecution\n" +
                "GUIUpdateExecution\n";
        int delay = 2000; //2 seconds delay
        dispatcher.execute("dispatcher run -d -r " + delay);
        //Waiting for all the events to be executed in time
        waitFor(WAITING_TIME + delay);
        Assert.assertEquals(expected,outContent.toString().replaceAll("\r",""));

    }
}
