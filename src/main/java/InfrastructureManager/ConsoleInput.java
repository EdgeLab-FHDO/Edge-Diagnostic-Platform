package InfrastructureManager;

import java.util.Scanner;

/**
 * Class representing input from the console as a form of MasterInputInterface
 */
public class ConsoleInput implements MasterInputInterface {
    private final Scanner IN = new Scanner(System.in);

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
