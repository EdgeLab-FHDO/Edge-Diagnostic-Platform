package InfrastructureManager;

/**
 * Class implementing MasterOutput, for utilities within the master
 */
public class MasterUtility implements MasterOutput {
    /**
     * Out method implementation according to MasterOutput interface, which gets commands from the master
     * @param response Response coming from the master.
     * @throws IllegalArgumentException If the command is not defined or is missing arguments
     */
    @Override
    public void out(String response) {
        String[] command = response.split(" ");
        if (command[0].equals("util")) {
            try {
                switch (command[1]){
                    case "exit" :
                        Master.getInstance().exitAll();
                        break;
                    case "runRunner":
                        Master.getInstance().startRunnerThread(command[2]);
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid Command for Utility Output");
                }
            } catch (IndexOutOfBoundsException e) {
                throw new IllegalArgumentException("Arguments missing for command - MasterUtility");
            }
        }
    }
}
