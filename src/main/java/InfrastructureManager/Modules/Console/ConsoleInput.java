package InfrastructureManager.Modules.Console;

import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleExecutionException;
import InfrastructureManager.ModuleManagement.ModuleInput;

import java.util.Scanner;

/**
 * Class representing input from the console as a form of ModuleInput
 */
public class ConsoleInput extends ModuleInput {
    private final Scanner IN = new Scanner(System.in);

    public ConsoleInput(String name) {
        super(name);
    }
    /**
     * Method for reading from the console, blocks by terms of the underlining Scanner class instance
     * @return String object containing the last line inputted in the console
     */
    @Override
    public String read() {
        System.out.println("Input >");
        return IN.nextLine(); //Wraps the nextLine method from the console scanner
    }

    @Override
    public void response(ModuleExecutionException outputException) {
        if (outputException != null) {
            outputException.printStackTrace();
        }
    }
}
