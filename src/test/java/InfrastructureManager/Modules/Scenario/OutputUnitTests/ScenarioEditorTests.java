package InfrastructureManager.Modules.Scenario.OutputUnitTests;

import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleExecutionException;
import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;
import InfrastructureManager.Modules.CommonTestingMethods;
import InfrastructureManager.Modules.Scenario.Event;
import InfrastructureManager.Modules.Scenario.Exception.Output.EmptyEventListException;
import InfrastructureManager.Modules.Scenario.Exception.Output.ScenarioEditorException;
import InfrastructureManager.Modules.Scenario.Scenario;
import InfrastructureManager.Modules.Scenario.ScenarioEditor;
import InfrastructureManager.Modules.Scenario.ScenarioModule;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.stream.Collectors;


public class ScenarioEditorTests {

    private final ScenarioModule module;
    private final ScenarioEditor editor;
    private final String originalContent = "deploy_application" + "node_request" + "update_gui";

    public ScenarioEditorTests() throws IOException {
        module = new ScenarioModule();
        module.setName("test_module");
        InjectableValues inject = new InjectableValues.Std().addValue(ImmutablePlatformModule.class, module);
        String scenarioPath = "src/test/resources/Modules/Scenario/dummyScenario.json";
        ObjectMapper mapper = new ObjectMapper();
        mapper.setInjectableValues(inject);
        Scenario scenario = mapper.readValue(new File(scenarioPath), Scenario.class);
        editor = new ScenarioEditor(module,"test_module.editor", scenario);
    }

    @Test
    public void scenarioIsCorrectlyLoaded() {
        String result = editor.getScenario().getEventList()
                .stream()
                .map(Event::getCommand)
                .collect(Collectors.joining());
        Assert.assertEquals(originalContent, result);
    }

    @Test
    public void addFirstEventTest() throws ModuleExecutionException {
        editor.execute("editor addEvent test_event0 1000");
        String expected = originalContent + "test_event0";
        String result = getConcatenatedEventCommandsInScenario();
        Assert.assertEquals(expected, result);

    }

    @Test
    public void addAnotherEventTest() throws ModuleExecutionException {
        editor.execute("editor addEvent test_event0 1000");
        editor.execute("editor addEvent test_event1 2000");
        String expected = originalContent + "test_event0" + "test_event1";
        String result = getConcatenatedEventCommandsInScenario();
        Assert.assertEquals(expected, result);
    }

    @Test
    public void deleteEventTest() throws ModuleExecutionException {
        editor.execute("editor addEvent test_event0 1000");
        editor.execute("editor deleteEvent");
        String result = getConcatenatedEventCommandsInScenario();
        Assert.assertEquals(originalContent, result);
    }

    @Test
    public void deleteEventOnEmptyEventListThrowsException() {
        Scenario otherScenario = new Scenario(module);
        ScenarioEditor newEditor = new ScenarioEditor(module,"test_module.other_editor",otherScenario);
        String command = "editor deleteEvent";
        String expected = "Event list is empty!";
        assertExceptionInOutput(EmptyEventListException.class,expected,command, newEditor);
    }

    @Test
    public void saveToFileTest() throws ModuleExecutionException {
        File scenarioFile = new File("src/test/resources/Modules/Scenario/test_module.json");
        editor.execute("editor toFile src/test/resources/Modules/Scenario/");
        Assert.assertTrue(scenarioFile.exists());
        scenarioFile.deleteOnExit();
    }

    @Test
    public void invalidCommandThrowsException() {
        String command = "editor notACommand";
        String expected = "Invalid command notACommand for ScenarioEditor";
        assertExceptionInOutput(ScenarioEditorException.class, expected, command, editor);
    }

    @Test
    public void incompleteCommandThrowsException() {
        String command = "editor toFile";
        String expected = "Arguments missing for command " + command + " to ScenarioEditor";
        assertExceptionInOutput(ScenarioEditorException.class, expected, command, editor);
    }

    private String getConcatenatedEventCommandsInScenario() {
        return editor.getScenario().getEventList()
                .stream()
                .map(Event::getCommand)
                .collect(Collectors.joining());
    }

    private void assertExceptionInOutput(Class<? extends  Exception> exceptionClass,
                                         String expectedMessage, String command,
                                         ScenarioEditor anEditor) {
        CommonTestingMethods.assertException(exceptionClass, expectedMessage, () -> anEditor.execute(command));
    }

}
