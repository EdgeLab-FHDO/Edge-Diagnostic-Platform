package InfrastructureManager.Modules.Console;

import InfrastructureManager.MasterInput;

import java.util.Scanner;

/**
 * Class representing input from the console as a form of MasterInputInterface
 */
public class ConsoleInput extends MasterInput {
    private final Scanner IN = new Scanner(System.in);

    public ConsoleInput(String name) {
        super(name);
    }

    @Override
    protected String getSingleReading() {
        return null;
    }

    @Override
    protected void storeSingleReading(String reading){}

    /**
     * Method for reading from the console
     * @return String object containing the last line inputted in the console
     */
    @Override
    public String read() {
        System.out.println("Input >");
        return IN.nextLine(); //Wraps the nextLine method from the console scanner
    }
}
