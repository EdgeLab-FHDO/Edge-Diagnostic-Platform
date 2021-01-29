package InfrastructureManager.Modules.Scenario.OutputUnitTests;

import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleExecutionException;
import InfrastructureManager.Modules.CommonTestingMethods;
import InfrastructureManager.Modules.Scenario.Event;
import InfrastructureManager.Modules.Scenario.Exception.Output.EmptyEventListException;
import InfrastructureManager.Modules.Scenario.Exception.Output.ScenarioEditorException;
import InfrastructureManager.Modules.Scenario.ScenarioEditor;
import InfrastructureManager.Modules.Scenario.ScenarioModule;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.stream.Collectors;


public class ScenarioEditorTests {

    ScenarioModule module = new ScenarioModule();
    ScenarioEditor editor = new ScenarioEditor(module,"test_module.editor");

    @Test
    public void createScenarioTest() throws ModuleExecutionException {
        module.setName("test_module");
        editor.execute("editor create");
        Assert.assertEquals("test_module.scenario", editor.getScenario().getName());
    }

    @Test
    public void addFirstEventTest() throws ModuleExecutionException {
        module.setName("test_module");
        editor.execute("editor create");
        editor.execute("editor addEvent test_event0 1000");
        String expected = "test_event0";
        String result = getConcatenatedEventCommandsInScenario();
        Assert.assertEquals(expected, result);

    }

    @Test
    public void addAnotherEventTest() throws ModuleExecutionException {
        module.setName("test_module");
        editor.execute("editor create");
        editor.execute("editor addEvent test_event0 1000");
        editor.execute("editor addEvent test_event1 2000");
        String expected = "test_event0" + "test_event1";
        String result = getConcatenatedEventCommandsInScenario();
        Assert.assertEquals(expected, result);
    }

    @Test
    public void deleteEventTest() throws ModuleExecutionException {
        module.setName("test_module");
        editor.execute("editor create");
        editor.execute("editor addEvent test_event0 1000");
        editor.execute("editor addEvent test_event1 2000");
        editor.execute("editor deleteEvent");
        String expected = "test_event0";
        String result = getConcatenatedEventCommandsInScenario();
        Assert.assertEquals(expected, result);
    }

    @Test
    public void deleteEventOnEmptyEventListThrowsException() throws ModuleExecutionException {
        module.setName("test_module");
        editor.execute("editor create");
        String command = "editor deleteEvent";
        String expected = "Event list is empty!";
        assertExceptionInOutput(EmptyEventListException.class,expected,command);
    }

    @Test
    public void saveToFileTest() throws ModuleExecutionException {
        module.setName("test_module");
        File scenarioFile = new File("src/test/resources/Modules/Scenario/test_module.json");
        editor.execute("editor create");
        editor.execute("editor addEvent test_event0 1000");
        editor.execute("editor addEvent test_event1 2000");
        editor.execute("editor toFile src/test/resources/Modules/Scenario/");
        Assert.assertTrue(scenarioFile.exists());
        scenarioFile.deleteOnExit();
    }

    @Test
    public void loadFromFileTest() throws ModuleExecutionException {
        editor.execute("editor fromFile src/test/resources/Modules/Scenario/dummyScenario.json");
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
        String command = "editor fromFile";
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
        CommonTestingMethods.assertException(exceptionClass, expectedMessage, () -> editor.execute(command));
    }

}
