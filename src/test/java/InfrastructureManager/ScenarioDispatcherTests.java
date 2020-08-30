package InfrastructureManager;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;


public class ScenarioDispatcherTests {
    ScenarioDispatcher dispatcher = new ScenarioDispatcher();
    final String SCENARIO_PATH = "src/test/resources/dummyScenario.json";
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

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

    @Before //For testing the Standard Output
    public void captureStream() {
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void restoreStream() {
        System.setOut(System.out);
    }

    @Test
    public void runScenarioTest() throws InterruptedException {

        dispatcher.out("dispatcher fromFile " + SCENARIO_PATH);
        dispatcher.out("dispatcher run");
        String expected = "helmChartExecution\n" + "matchMakingExecution\n" +
                "GUIUpdateExecution\n";
        //Waiting for a very small time, necessary otherwise wont read
        Thread.sleep(11000); //If events are delayed this has to be modified
        Assert.assertEquals(expected,outContent.toString().replaceAll("\r",""));

    }

}
