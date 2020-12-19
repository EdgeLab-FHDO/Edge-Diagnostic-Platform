package InfrastructureManager;

import org.junit.*;
import org.junit.rules.ExpectedException;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;


public class ScenarioDispatcherTests {
    ScenarioDispatcher dispatcher = new ScenarioDispatcher("scenario_dispatcher");
    final String SCENARIO_PATH = "src/test/resources/ScenarioResources/dummyScenario.json";
    static final long WAITING_TIME = 12000; //If events are delayed this has to be modified
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @BeforeClass
    public static void configureMaster() {
        Master.changeConfigPath("src/test/resources/ScenarioResources/ScenarioConfiguration.json");
        Master.resetInstance();
    }

    @Test
    public void loadScenarioFromFileTest() {
        dispatcher.out("dispatcher fromFile " + SCENARIO_PATH);
        String expected = "deploy_application" + "node_request" + "update_gui";
        StringBuilder result = new StringBuilder();
        for (Event e : dispatcher.getScenario().getEventList()) {
            result.append(e.read());
        }
        Assert.assertEquals(result.toString(),expected);
    }

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void invalidCommandThrowsException() {
        exceptionRule.expect(IllegalArgumentException.class);
        exceptionRule.expectMessage("Invalid command for ScenarioDispatcher");
        dispatcher.out("dispatcher notACommand");
    }
    @Test
    public void incompleteCommandThrowsException() {
        exceptionRule.expect(IllegalArgumentException.class);
        exceptionRule.expectMessage("Arguments missing for command  - ScenarioDispatcher");
        dispatcher.out("dispatcher fromFile"); //Missing the name
    }
    @Test
    public void wrongRunWithDelayCommandThrowsException() {
        exceptionRule.expect(IllegalArgumentException.class);
        exceptionRule.expectMessage("Invalid run command");
        dispatcher.out("dispatcher run -d wrongCommand");
    }
    @Test
    public void incompleteRunWithDelayCommandThrowsException() {
        exceptionRule.expect(IllegalArgumentException.class);
        exceptionRule.expectMessage("Arguments missing for command  - ScenarioDispatcher");
        dispatcher.out("dispatcher run -d"); //Missing one argument
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
    public void runScenarioTest() {

        String expected = "helmChartExecution\n" + "matchMakingExecution\n" +
                "GUIUpdateExecution\n";
        dispatcher.out("dispatcher fromFile " + SCENARIO_PATH);
        dispatcher.out("dispatcher run");
        //Waiting for all the events to be executed in time
        waitFor(WAITING_TIME);
        Assert.assertEquals(expected,outContent.toString().replaceAll("\r",""));

    }

    @Test
    public void runScenarioRelativeDelayTest(){
        String expected = "helmChartExecution\n" + "matchMakingExecution\n" +
                "GUIUpdateExecution\n";
        int delay = 2000; //2 seconds delay
        dispatcher.out("dispatcher fromFile " + SCENARIO_PATH);
        dispatcher.out("dispatcher run -d -r " + delay);
        //Waiting for all the events to be executed in time
        waitFor(WAITING_TIME + delay);
        Assert.assertEquals(expected,outContent.toString().replaceAll("\r",""));

    }
}
