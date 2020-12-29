package InfrastructureManager;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;


public class ScenarioEditorTests {

    ScenarioEditor editor = new ScenarioEditor("scenario_editor");
    final String scenarioName = "testScenario";

    @BeforeClass
    public static void configureMaster() {
        Master.changeConfigPath("src/test/resources/ScenarioResources/ScenarioConfiguration.json");
        Master.resetInstance();
    }

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
        File scenarioFile = new File("src/test/resources/ScenarioResources/" + scenarioName + ".json");
        if (scenarioFile.exists()) { //Delete the last test file, each time a test is run
            scenarioFile.delete();
        }
        editor.out("editor create " + scenarioName);
        editor.out("editor addEvent test_event0 1000");
        editor.out("editor addEvent test_event1 2000");
        editor.out("editor toFile src/test/resources/ScenarioResources/");

        Assert.assertTrue(scenarioFile.exists());
    }

    @Test
    public void loadFromFileTest() {
        editor.out("editor fromFile src/test/resources/ScenarioResources/dummyScenario.json");
        String expected = "deploy_application" + "node_request" + "update_gui";
        StringBuilder result = new StringBuilder();
        for (Event e : editor.getScenario().getEventList()) {
            result.append(e.read());
        }
        Assert.assertEquals(result.toString(),expected);
    }

    public void assertException(Class<? extends  Throwable> exceptionClass, String command ,String expectedMessage) {
        var e = Assert.assertThrows(exceptionClass, () -> editor.out(command));
        Assert.assertEquals(expectedMessage, e.getMessage());
    }

    @Test
    public void invalidCommandThrowsException() {
        assertException(IllegalArgumentException.class, "editor notACommand", "Invalid command for ScenarioEditor");
    }

    @Test
    public void incompleteCommandThrowsException() {
        assertException(IllegalArgumentException.class, "editor create", "Arguments missing for command  - ScenarioEditor");
    }
}
