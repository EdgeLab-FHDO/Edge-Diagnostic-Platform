package InfrastructureManager.Modules.Utility;

import InfrastructureManager.Master;
import InfrastructureManager.ModuleManagement.Exception.Creation.ModuleManagerException;
import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleNotFoundException;
import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;
import InfrastructureManager.ModuleManagement.ModuleManager;
import InfrastructureManager.ModuleManagement.PlatformOutput;
import InfrastructureManager.Modules.Utility.Exception.ModuleController.ModuleControllerException;

/**
 * Class implementing MasterOutput, for utilities within the master
 */
public class ModuleController extends PlatformOutput {

    public ModuleController(ImmutablePlatformModule module, String name) {
        super(module,name);
    }


    /**
     Out method implementation according to MasterOutput interface, which gets commands from the master
     * @param response Response coming from the master.
     * @throws ModuleNotFoundException If the module which is aimed to be controlled is not configured
     * @throws ModuleControllerException If the command passed is invalid or incomplete
     */
    @Override
    public void execute(String response) throws ModuleNotFoundException, ModuleControllerException {
        String[] command = response.split(" ");
        if (command[0].equals("util")) {
            try {
                ModuleManager manager = Master.getInstance().getManager();
                switch (command[1]) {
                    case "exit" -> manager.exitAllModules();
                    case "startModule" -> manager.startModule(command[2]);
                    case "pauseModule" -> manager.pauseModule(command[2]);
                    case "resumeModule" -> manager.resumeModule(command[2]);
                    case "stopModule" -> manager.stopModule(command[2]);
                    case "pauseAllModules" -> manager.pauseAllModules();
                    case "resumeAllModules" -> manager.resumeAllModules();
                    default -> throw new ModuleControllerException("Invalid Command " + command[1]
                            + " for ModuleController output");
                }
            } catch (IndexOutOfBoundsException e) {
                throw new ModuleControllerException("Arguments missing for command " + response
                        + " to ModuleController");
            } catch (ModuleManagerException e) {
                throw new ModuleControllerException("Error while fetching manager", e);
            }
        }
    }
}
