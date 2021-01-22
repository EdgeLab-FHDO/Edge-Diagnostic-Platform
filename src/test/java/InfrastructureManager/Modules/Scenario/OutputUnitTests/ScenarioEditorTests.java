package InfrastructureManager.Modules.Scenario.OutputUnitTests;

import InfrastructureManager.Master;
import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleExecutionException;
import InfrastructureManager.Modules.CommonTestingMethods;
import InfrastructureManager.Modules.Scenario.Event;
import InfrastructureManager.Modules.Scenario.Exception.Output.EmptyEventListException;
import InfrastructureManager.Modules.Scenario.Exception.Output.ScenarioEditorException;
import InfrastructureManager.Modules.Scenario.ScenarioEditor;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.util.stream.Collectors;


public class ScenarioEditorTests {

    ScenarioEditor editor = new ScenarioEditor("dummy.editor");
    final String scenarioName = "test";

    @BeforeClass
    public static void configureMaster() {
        Master.changeConfigPath("src/test/resources/ScenarioResources/ScenarioConfiguration.json");
        Master.resetInstance();
        Master.getInstance().startAllModules();
    }

    @Test
    public void createScenarioTest() throws ModuleExecutionException {
        editor.write("editor create " + scenarioName);
        Assert.assertEquals(scenarioName + ".scenario", editor.getScenario().getName());
    }

    @Test
    public void addFirstEventTest() throws ModuleExecutionException {
        editor.write("editor create " + scenarioName);
        editor.write("editor addEvent test_event0 1000");
        String expected = "test_event0";
        String result = getConcatenatedEventCommandsInScenario();
        Assert.assertEquals(expected, result);

    }

    @Test
    public void addAnotherEventTest() throws ModuleExecutionException {
        editor.write("editor create " + scenarioName);
        editor.write("editor addEvent test_event0 1000");
        editor.write("editor addEvent test_event1 2000");
        String expected = "test_event0" + "test_event1";
        String result = getConcatenatedEventCommandsInScenario();
        Assert.assertEquals(expected, result);
    }

    @Test
    public void deleteEventTest() throws ModuleExecutionException {
        editor.write("editor create " + scenarioName);
        editor.write("editor addEvent test_event0 1000");
        editor.write("editor addEvent test_event1 2000");
        editor.write("editor deleteEvent");
        String expected = "test_event0";
        String result = getConcatenatedEventCommandsInScenario();
        Assert.assertEquals(expected, result);
    }

    @Test
    public void deleteEventOnEmptyEventListThrowsException() throws ModuleExecutionException {
        editor.write("editor create " + scenarioName);
        String command = "editor deleteEvent";
        String expected = "Event list is empty!";
        assertExceptionInOutput(EmptyEventListException.class,expected,command);
    }

    @Test
    public void saveToFileTest() throws ModuleExecutionException {
        File scenarioFile = new File("src/test/resources/ScenarioResources/" + scenarioName + ".json");
        editor.write("editor create " + scenarioName);
        editor.write("editor addEvent test_event0 1000");
        editor.write("editor addEvent test_event1 2000");
        editor.write("editor toFile src/test/resources/ScenarioResources/");
        Assert.assertTrue(scenarioFile.exists());
        scenarioFile.deleteOnExit();
    }

    @Test
    public void loadFromFileTest() throws ModuleExecutionException {
        editor.write("editor fromFile src/test/resources/ScenarioResources/dummyScenario.json");
        String expected = "deploy_application" + "node_request" + "update_gui";
        String result = getConcatenatedEventCommandsInScenario();
        Assert.assertEquals(expected, result);
    }



    @Test
    public void invalidCommandThrowsException() {
        String command = "editor notACommand";
        String expected = "Invalid command notACommand for ScenarioEditor";
        assertExceptionInOutput(ScenarioEditorException.class, expected, command);
    }

    @Test
    public void incompleteCommandThrowsException() {
        String command = "editor create";
        String expected = "Arguments missing for command " + command + " to ScenarioEditor";
        assertExceptionInOutput(ScenarioEditorException.class, expected, command);
    }

    private String getConcatenatedEventCommandsInScenario() {
        return editor.getScenario().getEventList()
                .stream()
                .map(Event::getCommand)
                .collect(Collectors.joining());
    }

    private void assertExceptionInOutput(Class<? extends  Exception> exceptionClass, String expectedMessage, String command) {
        CommonTestingMethods.assertException(exceptionClass, expectedMessage, () -> editor.write(command));
    }

}
