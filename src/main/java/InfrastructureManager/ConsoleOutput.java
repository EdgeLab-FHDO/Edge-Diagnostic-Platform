package InfrastructureManager;

/**
 * Class representing output to the console as a form of MasterOutput
 */
public class ConsoleOutput extends MasterOutput {

    public ConsoleOutput(String name) {
        super(name);
    }

    /**
     * Method for outputting to the console
     * @param response Message to be outputted to the console, the command has to be preceded by "console"
     * @throws IllegalArgumentException If the command is not correctly formatted
     */
    @Override
    public void out(String response) {
        String[] command = response.split(" ");
        if (command[0].equals("console")) { //The commands must come like "console command"
            try {
                System.out.println(command[1]);
            } catch (IndexOutOfBoundsException e) {
                throw new IllegalArgumentException("Arguments missing for command - ConsoleOutput");
            }
        }
    }
}
