package InfrastructureManager;

/**
 * Class representing output to the console as a form of MasterOutput
 */
public class ConsoleOutput implements MasterOutput {
    /**
     * Method for outputting to the console
     * @param response Message to be outputted to the console, the command has to be preceded by "console"
     */
    @Override
    public void out(String response) {
        String[] command = response.split(" ");
        if (command[0].equals("console")) {
            System.out.println(command[1]);
        }
    }
}
