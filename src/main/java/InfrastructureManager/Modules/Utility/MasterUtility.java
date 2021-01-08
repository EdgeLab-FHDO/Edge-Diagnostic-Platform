package InfrastructureManager.Modules.Utility;

import InfrastructureManager.Master;
import InfrastructureManager.MasterOutput;

/**
 * Class implementing MasterOutput, for utilities within the master
 */
public class MasterUtility extends MasterOutput {

    public MasterUtility(String name) {
        super(name);
    }

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
                switch (command[1]) {
                    case "exit" -> Master.getInstance().exitAll();
                    case "startModule" -> Master.getInstance().startModule(command[2]);
                    case "pauseModule" -> Master.getInstance().pauseModule(command[2]);
                    case "resumeModule" -> Master.getInstance().resumeModule(command[2]);
                    case "pauseAllModules" -> Master.getInstance().pauseAllModules();
                    case "resumeAllModules" -> Master.getInstance().resumeAllModules();
                    default -> throw new IllegalArgumentException("Invalid Command for Utility Output");
                }
            } catch (IndexOutOfBoundsException e) {
                throw new IllegalArgumentException("Arguments missing for command - MasterUtility");
            }
        }
    }
}
