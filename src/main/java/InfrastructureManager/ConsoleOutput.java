package InfrastructureManager;

/**
 * Class representing output to the console as a form of MasterOutput
 */
public class ConsoleOutput implements MasterOutput {
    /**
     * Method for outputting to the console
     * @param response Message to be outputted to the console
     */
    @Override
    public void out(String response) {
        System.out.println(response);
    }
}
