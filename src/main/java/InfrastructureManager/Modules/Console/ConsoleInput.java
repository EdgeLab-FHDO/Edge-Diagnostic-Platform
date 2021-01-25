package InfrastructureManager.Modules.Console;

import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleExecutionException;
import InfrastructureManager.ModuleManagement.ModuleInput;
import InfrastructureManager.ModuleManagement.PlatformModule;

import java.util.Scanner;

/**
 * Class representing input from the console as a form of ModuleInput
 */
public class ConsoleInput extends ModuleInput {
    private final Scanner IN = new Scanner(System.in);

    public ConsoleInput(PlatformModule module, String name) {
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
        this.getOwnerModule().getDebugInput().debug(reading);
        return reading; //Wraps the nextLine method from the console scanner
    }

    @Override
    public void response(ModuleExecutionException outputException) {
        if (outputException != null) {
            outputException.printStackTrace();
        }
    }
}
