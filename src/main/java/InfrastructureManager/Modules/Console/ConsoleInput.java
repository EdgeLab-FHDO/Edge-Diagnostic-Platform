package InfrastructureManager.Modules.Console;

import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleExecutionException;
import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;
import InfrastructureManager.ModuleManagement.PlatformInput;

import java.util.Scanner;

/**
 * Class representing input from the console as a form of PlatformInput
 */
public class ConsoleInput extends ConsoleModuleObject implements PlatformInput {
    private final Scanner IN = new Scanner(System.in);

    /**
     * Creates a new ConsoleInput
     * @param module Owner module of this input
     * @param name Name of this input
     */
    public ConsoleInput(ImmutablePlatformModule module, String name) {
        super(module,name);
    }

    /**
     * Method for reading from the console, blocks by terms of the underlining Scanner class instance
     * @return String object containing the last line inputted in the console
     */
    @Override
    public String read() {
        System.out.println("Input >");
        String reading = IN.nextLine();
        this.getLogger().debug(this.getName()+" - "+ reading);
        return reading; //Wraps the nextLine method from the console scanner
    }

    /**
     * Handles exceptions in the execution process. In this particular input, the exception message is printed to the console.
     * @param outputException Exception happening in the platform's execution process. This value is null if the process went correctly (No exception was raised)
     */
    @Override
    public void response(ModuleExecutionException outputException) {
        if (outputException != null) {
            outputException.printStackTrace();
        }
    }
}
