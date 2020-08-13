package InfrastructureManager;

import java.util.Scanner;

/**
 * Class representing input from the console as a form of MasterInput
 */
public class ConsoleInput implements MasterInput{
    private final Scanner IN = new Scanner(System.in);

    /**
     * Method for reading from the console
     * @return String object containing the last line inputted in the console
     */
    @Override
    public String read() {
        return IN.nextLine(); //Wraps the nextLine method from the console scanner
    }
}
