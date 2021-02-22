package InfrastructureManager.Modules.Console;

import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;
import InfrastructureManager.ModuleManagement.PlatformOutput;
import InfrastructureManager.Modules.Console.Exception.ConsoleOutputException;

import java.util.Arrays;

/**
 * Class representing output to the console as a form of PlatformOutput
 */
public class ConsoleOutput extends ConsoleModuleObject implements PlatformOutput {

    /**
     * Creates a new ConsoleOutput
     * @param module Owner module of this output
     * @param name Name of this output
     */
    public ConsoleOutput(ImmutablePlatformModule module, String name) {
        super(module, name);
    }

    /**
     * Method for outputting to the console
     * @param response Message to be outputted to the console.
     *                The command has to be preceded by "console".
     *                 For example: "console hello" -> prints hello.
     *                 If more than one argument is passed, they should be separated by spaces and will be printed in the same way
     * @throws ConsoleOutputException if the command is missing arguments (no argument passed after "console")
     */
    @Override
    public void execute(String response) throws ConsoleOutputException {
        String[] command = response.split(" ");
        if (command[0].equals("console")) { //The commands must come like "console command"
            try {
                String result = String.join(" ",Arrays.copyOfRange(command,1,command.length)); //Grab all arguments passed and space them
                System.out.println(result);
            } catch (IndexOutOfBoundsException e) {
                throw new ConsoleOutputException("Arguments missing for command" + response + " to ConsoleOutput");
            }
        }
    }
}
