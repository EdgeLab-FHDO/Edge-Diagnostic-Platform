package InfrastructureManager;

import org.junit.*;

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

    @Test
    public void invalidCommandThrowsException() {
        String command = "dispatcher notACommand";
        String expected = "Invalid command for ScenarioDispatcher";
        assertException(IllegalArgumentException.class,command,expected);
    }
    @Test
    public void incompleteCommandThrowsException() {
        String command = "dispatcher fromFile";
        String expected = "Arguments missing for command  - ScenarioDispatcher";
        assertException(IllegalArgumentException.class,command,expected);
    }
    @Test
    public void wrongRunWithDelayCommandThrowsException() {
        String command = "dispatcher run -d wrongCommand";
        String expected = "Invalid run command";
        assertException(IllegalArgumentException.class,command,expected);
    }
    @Test
    public void incompleteRunWithDelayCommandThrowsException() {
        String command = "dispatcher run -d";
        String expected = "Arguments missing for command  - ScenarioDispatcher";
        assertException(IllegalArgumentException.class,command,expected);
    }

    public void assertException(Class<? extends  Throwable> exceptionClass, String command ,String expectedMessage) {
        var e = Assert.assertThrows(exceptionClass, () -> dispatcher.out(command));
        Assert.assertEquals(expectedMessage, e.getMessage());
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
