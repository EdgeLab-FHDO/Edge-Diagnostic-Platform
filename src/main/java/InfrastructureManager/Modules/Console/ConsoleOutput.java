package InfrastructureManager.Modules.Console;

import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;
import InfrastructureManager.ModuleManagement.ModuleOutput;
import InfrastructureManager.ModuleManagement.PlatformModule;
import InfrastructureManager.Modules.Console.Exception.ConsoleOutputException;

import java.util.Arrays;

/**
 * Class representing output to the console as a form of MasterOutput
 */
public class ConsoleOutput extends ModuleOutput {

    public ConsoleOutput(ImmutablePlatformModule module, String name) {
        super(module, name);
    }
    /**
     * Method for outputting to the console
     * @param response Message to be outputted to the console, the command has to be preceded by "console"
     * @throws ConsoleOutputException if the command is missing arguments
     */
    @Override
    public void execute(String response) throws ConsoleOutputException {
        String[] command = response.split(" ");
        if (command[0].equals("console")) { //The commands must come like "console command"
            try {
                String result = String.join(" ",Arrays.copyOfRange(command,1,command.length));
                System.out.println(result);
            } catch (IndexOutOfBoundsException e) {
                throw new ConsoleOutputException("Arguments missing for command" + response + " to ConsoleOutput");
            }
        }
    }
}
