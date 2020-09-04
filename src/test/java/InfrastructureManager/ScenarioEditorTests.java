package InfrastructureManager;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.File;


public class ScenarioEditorTests {

    ScenarioEditor editor = new ScenarioEditor();
    final String scenarioName = "testScenario";

    @Test
    public void createScenarioTest() {
        editor.out("editor create " + scenarioName);
        Assert.assertEquals(editor.getScenario().getName(),scenarioName);
    }

    @Test
    public void addFirstEventTest() {
        editor.out("editor create " + scenarioName);
        editor.out("editor addEvent test_event0 1000");
        String expected = "test_event0";
        StringBuilder result = new StringBuilder();
        for (Event e : editor.getScenario().getEventList()) {
            result.append(e.read());
        }
        Assert.assertEquals(result.toString(),expected);
    }

    @Test
    public void addAnotherEventTest() {
        editor.out("editor create " + scenarioName);
        editor.out("editor addEvent test_event0 1000");
        editor.out("editor addEvent test_event1 2000");
        String expected = "test_event0" + "test_event1";
        StringBuilder result = new StringBuilder();
        for (Event e : editor.getScenario().getEventList()) {
            result.append(e.read());
        }
        Assert.assertEquals(result.toString(),expected);
    }

    @Test
    public void deleteEventTest() {
        editor.out("editor create " + scenarioName);
        editor.out("editor addEvent test_event0 1000");
        editor.out("editor addEvent test_event1 2000");
        editor.out("editor deleteEvent");
        String expected = "test_event0";
        StringBuilder result = new StringBuilder();
        for (Event e : editor.getScenario().getEventList()) {
            result.append(e.read());
        }
        Assert.assertEquals(result.toString(),expected);
    }

    @Test
    public void saveToFileTest(){
        File scenarioFile = new File("src/test/resources/" + scenarioName + ".json");
        if (scenarioFile.exists()) { //Delete the last test file, each time a test is run
            scenarioFile.delete();
        }
        editor.out("editor create " + scenarioName);
        editor.out("editor addEvent test_event0 1000");
        editor.out("editor addEvent test_event1 2000");
        editor.out("editor toFile src/test/resources/");

        Assert.assertTrue(scenarioFile.exists());
    }

    @Test
    public void loadFromFileTest() {
        editor.out("editor fromFile src/test/resources/dummyScenario.json");
        String expected = "deploy_application" + "node_request" + "update_gui";
        StringBuilder result = new StringBuilder();
        for (Event e : editor.getScenario().getEventList()) {
            result.append(e.read());
        }
        Assert.assertEquals(result.toString(),expected);
    }

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void invalidCommandThrowsException() {
        exceptionRule.expect(IllegalArgumentException.class);
        exceptionRule.expectMessage("Invalid command for ScenarioEditor");
        editor.out("editor notACommand");
    }

    @Test
    public void incompleteCommandThrowsException() {
        exceptionRule.expect(IllegalArgumentException.class);
        exceptionRule.expectMessage("Arguments missing for command  - ScenarioEditor");
        editor.out("editor create"); //Missing the name
    }
}
