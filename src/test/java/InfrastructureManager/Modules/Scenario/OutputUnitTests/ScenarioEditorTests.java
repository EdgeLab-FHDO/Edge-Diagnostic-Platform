package InfrastructureManager.Modules.Scenario.OutputUnitTests;

import InfrastructureManager.Configuration.Exception.ConfigurationException;
import InfrastructureManager.Master;
import InfrastructureManager.ModuleManagement.Exception.Creation.ModuleManagerException;
import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleExecutionException;
import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleNotFoundException;
import InfrastructureManager.ModuleManagement.ModuleManager;
import InfrastructureManager.Modules.CommonTestingMethods;
import InfrastructureManager.Modules.Scenario.Event;
import InfrastructureManager.Modules.Scenario.Exception.Output.EmptyEventListException;
import InfrastructureManager.Modules.Scenario.Exception.Output.ScenarioEditorException;
import InfrastructureManager.Modules.Scenario.Scenario;
import InfrastructureManager.Modules.Scenario.ScenarioEditor;
import InfrastructureManager.Modules.Scenario.ScenarioModule;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.util.stream.Collectors;


public class ScenarioEditorTests {

    private static ScenarioModule module;
    private ScenarioEditor editor;
    private final String originalContent = "deploy_application" + "node_request" + "update_gui";

    @BeforeClass
    public static void setUpMasterAndStartServer() throws ModuleNotFoundException, ConfigurationException, ModuleManagerException {
        Master.resetInstance();
        Master.getInstance().configure("src/test/resources/Modules/Scenario/ScenarioConfiguration.json");
        Master.getInstance().getManager().startModule("dummy");
        module = (ScenarioModule) Master.getInstance().getManager().getModules().stream()
                .filter(m -> m.getName().equals("dummy"))
                .findFirst()
                .orElseThrow();

    }

    private static ScenarioModule findModule(ModuleManager manager) throws ModuleNotFoundException, ModuleManagerException {
        return (ScenarioModule) manager.getModules().stream()
                .filter(m -> m.getName().equals("dummy"))
                .findFirst()
                .orElseThrow(() -> new ModuleNotFoundException("There is no module for dummy scenario"));
    }

    @Before
    public void init() {
        editor = new ScenarioEditor(module,"dummy.editor");
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
    public void deleteEventTest() throws ModuleExecutionException {
        editor.execute("editor addEvent test_event0 1000");
        editor.execute("editor deleteEvent");
        String result = getConcatenatedEventCommandsInScenario();
        Assert.assertEquals(originalContent, result);
    }

    @Test
    public void addFirstEventTest() throws ModuleExecutionException {
        editor.execute("editor addEvent test_event0 1000");
        String expected = originalContent + "test_event0";
        String result = getConcatenatedEventCommandsInScenario();
        Assert.assertEquals(expected, result);
        editor.execute("editor deleteEvent");

    }

    @Test
    public void addAnotherEventTest() throws ModuleExecutionException {
        editor.execute("editor addEvent test_event0 1000");
        editor.execute("editor addEvent test_event1 2000");
        String expected = originalContent + "test_event0" + "test_event1";
        String result = getConcatenatedEventCommandsInScenario();
        Assert.assertEquals(expected, result);
        editor.execute("editor deleteEvent");
        editor.execute("editor deleteEvent");
    }



    /*@Test
    public void deleteEventOnEmptyEventListThrowsException() {
        Scenario otherScenario = new Scenario(module);
        ScenarioEditor newEditor = new ScenarioEditor(module,"test_module.other_editor");
        String command = "editor deleteEvent";
        String expected = "Event list is empty!";
        assertExcetionInOutput(EmptyEventListException.class,expected,command, newEditor);
    }*/

    @Test
    public void saveToFileTest() throws ModuleExecutionException {
        File scenarioFile = new File("src/test/resources/Modules/Scenario/dummy.json");
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
