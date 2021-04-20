package InfrastructureManager;

import InfrastructureManager.Configuration.Exception.ConfigurationException;
import InfrastructureManager.ModuleManagement.Exception.Creation.ModuleManagerException;
import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleNotFoundException;
import InfrastructureManager.ModuleManagement.ModuleDebugInput;
import InfrastructureManager.Modules.Console.ConsoleModule;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class DebugInputTests {

    ModuleDebugInput input = new ModuleDebugInput(null,"debug");

    @BeforeClass
    public static void setUpMasterAndStartServer() throws ModuleNotFoundException, ConfigurationException, ModuleManagerException {
        Master.resetInstance();
    }

    @Test
    public void blocksIfNoMessagePassed() throws InterruptedException {
        Thread inputRunner = new Thread(() -> {
            try {
                input.read();
            } catch (InterruptedException ignored) {}
        });
        inputRunner.start();
        Thread.sleep(5000); //Wait 5 seconds, and then check thread is still blocked
        Assert.assertEquals(Thread.State.WAITING, inputRunner.getState());
        inputRunner.interrupt();
    }

    @Test
    public void debugLevelMessagesTest() throws InterruptedException {
        String message = "A debug message";
        String expected = "fromDebug DEBUG-debug-debugLevelMessagesTest:38 -A debug message";
        input.debug(this.input.getName(), message);
        Assert.assertEquals(expected, input.read());
    }

    @Test
    public void warnLevelMessagesTest() throws InterruptedException {
        String message = "A warn message";
        String expected = "fromDebug WARN - A warn message";
        input.warn(message);
        Assert.assertEquals(expected, input.read());
    }

    @Test
    public void errorLevelMessagesTest() throws InterruptedException {
        String message = "An error message";
        String expected = "fromDebug ERROR - An error message";
        input.error(message);
        Assert.assertEquals(expected, input.read());
    }
}
