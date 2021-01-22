package InfrastructureManager.Modules.Utility.OutputUnitTests;

import InfrastructureManager.Master;
import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleNotFoundException;
import InfrastructureManager.Modules.CommonTestingMethods;
import InfrastructureManager.Modules.Utility.Exception.MasterController.MasterControllerException;
import InfrastructureManager.Modules.Utility.MasterController;
import org.junit.BeforeClass;
import org.junit.Test;

public class MasterControllerTests {

    private final MasterController output = new MasterController("util.control");

    @BeforeClass
    public static void setUp() {
        Master.changeConfigPath("src/test/resources/UtilityModule/UtilityModuleTestConfiguration.json");
        Master.resetInstance();
        Master.getInstance().startAllModules();
    }

    @Test
    public void startingNonExistingModuleThrowsException() {
        String command = "util startModule rest";
        String expected = "Module rest was not found";
        assertExceptionInOutput(ModuleNotFoundException.class, expected, command);
    }
    @Test
    public void pausingNonExistingModuleThrowsException() {
        String command = "util pauseModule rest";
        String expected = "Module rest was not found";
        assertExceptionInOutput(ModuleNotFoundException.class, expected, command);
    }
    @Test
    public void resumingNonExistingModuleThrowsException() {
        String command = "util resumeModule rest";
        String expected = "Module rest was not found";
        assertExceptionInOutput(ModuleNotFoundException.class, expected, command);
    }
    @Test
    public void stoppingNonExistingModuleThrowsException() {
        String command = "util stopModule rest";
        String expected = "Module rest was not found";
        assertExceptionInOutput(ModuleNotFoundException.class, expected, command);
    }

    @Test
    public void invalidCommandThrowsException() {
        String command = "util notACommand";
        String expected = "Invalid Command notACommand for MasterController output";
        assertExceptionInOutput(MasterControllerException.class, expected, command);
    }

    @Test
    public void incompleteCommandThrowsException() {
        String command = "util startModule";
        String expected = "Arguments missing for command " + command + " to MasterController";
        assertExceptionInOutput(MasterControllerException.class, expected, command);
    }

    private void assertExceptionInOutput(Class<? extends  Exception> exceptionClass, String expected, String command) {
        CommonTestingMethods.assertException(exceptionClass, expected, () -> output.write(command));
    }
}
