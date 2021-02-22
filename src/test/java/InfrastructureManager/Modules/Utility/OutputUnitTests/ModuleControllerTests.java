package InfrastructureManager.Modules.Utility.OutputUnitTests;

import InfrastructureManager.Configuration.Exception.ConfigurationException;
import InfrastructureManager.Master;
import InfrastructureManager.ModuleManagement.Exception.Creation.ModuleManagerException;
import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleNotFoundException;
import InfrastructureManager.Modules.CommonTestingMethods;
import InfrastructureManager.Modules.Utility.Exception.ModuleController.ModuleControllerException;
import InfrastructureManager.Modules.Utility.Output.ModuleController;
import InfrastructureManager.Modules.Utility.UtilityModule;
import org.junit.BeforeClass;
import org.junit.Test;

public class ModuleControllerTests {

    private final UtilityModule module = new UtilityModule();
    private final ModuleController output = new ModuleController(module,"util.control");

    @BeforeClass
    public static void setUp() throws ConfigurationException, ModuleManagerException {
        Master.resetInstance();
        Master.getInstance().configure("src/test/resources/Modules/Utility/UtilityModuleTestConfiguration.json");
        Master.getInstance().getManager().startAllModules();
    }

    @Test
    public void startingNonExistingModuleThrowsException() {
        String command = "util startModule mm";
        String expected = "Module mm was not found";
        assertExceptionInOutput(ModuleNotFoundException.class, expected, command);
    }
    @Test
    public void pausingNonExistingModuleThrowsException() {
        String command = "util pauseModule mm";
        String expected = "Module mm was not found";
        assertExceptionInOutput(ModuleNotFoundException.class, expected, command);
    }
    @Test
    public void resumingNonExistingModuleThrowsException() {
        String command = "util resumeModule mm";
        String expected = "Module mm was not found";
        assertExceptionInOutput(ModuleNotFoundException.class, expected, command);
    }
    @Test
    public void stoppingNonExistingModuleThrowsException() {
        String command = "util stopModule mm";
        String expected = "Module mm was not found";
        assertExceptionInOutput(ModuleNotFoundException.class, expected, command);
    }

    @Test
    public void invalidCommandThrowsException() {
        String command = "util notACommand";
        String expected = "Invalid Command notACommand for ModuleController output";
        assertExceptionInOutput(ModuleControllerException.class, expected, command);
    }

    @Test
    public void incompleteCommandThrowsException() {
        String command = "util startModule";
        String expected = "Arguments missing for command " + command + " to ModuleController";
        assertExceptionInOutput(ModuleControllerException.class, expected, command);
    }

    private void assertExceptionInOutput(Class<? extends  Exception> exceptionClass, String expected, String command) {
        CommonTestingMethods.assertException(exceptionClass, expected, () -> output.execute(command));
    }
}
