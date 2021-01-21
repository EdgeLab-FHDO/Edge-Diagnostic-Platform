package InfrastructureManager.Modules.Utility;

import InfrastructureManager.Master;
import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleNotFoundException;
import InfrastructureManager.ModuleManagement.ModuleOutput;
import InfrastructureManager.Modules.Utility.Exception.MasterController.MasterControllerException;

/**
 * Class implementing MasterOutput, for utilities within the master
 */
public class MasterController extends ModuleOutput {

    public MasterController(String name) {
        super(name);
    }


    /**
     Out method implementation according to MasterOutput interface, which gets commands from the master
     * @param response Response coming from the master.
     * @throws ModuleNotFoundException If the module which is aimed to be controlled is not configured
     * @throws MasterControllerException If the command passed is invalid or incomplete
     */
    @Override
    protected void out(String response) throws ModuleNotFoundException, MasterControllerException {
        String[] command = response.split(" ");
        if (command[0].equals("util")) {
            try {
                switch (command[1]) {
                    case "exit" -> Master.getInstance().exitAll();
                    case "startModule" -> Master.getInstance().startModule(command[2]);
                    case "pauseModule" -> Master.getInstance().pauseModule(command[2]);
                    case "resumeModule" -> Master.getInstance().resumeModule(command[2]);
                    case "stopModule" -> Master.getInstance().stopModule(command[2]);
                    case "pauseAllModules" -> Master.getInstance().pauseAllModules();
                    case "resumeAllModules" -> Master.getInstance().resumeAllModules();
                    default -> throw new MasterControllerException("Invalid Command" + command[1]
                            + " for MasterController output");
                }
            } catch (IndexOutOfBoundsException e) {
                throw new MasterControllerException("Arguments missing for command" + response
                        + " to MasterController");
            }
        }
    }
}
