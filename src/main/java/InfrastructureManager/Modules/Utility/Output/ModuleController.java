package InfrastructureManager.Modules.Utility.Output;

import InfrastructureManager.Master;
import InfrastructureManager.ModuleManagement.Exception.Creation.ModuleManagerException;
import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleNotFoundException;
import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;
import InfrastructureManager.ModuleManagement.ModuleManager;
import InfrastructureManager.ModuleManagement.PlatformOutput;
import InfrastructureManager.Modules.Utility.Exception.ModuleController.ModuleControllerException;
import InfrastructureManager.Modules.Utility.UtilityModuleObject;

/**
 * Class representing control over Modules as an output of the platform
 *
 * This type of output is used for controlling the deployment of the different modules as well as for exiting the platform.
 *
 * It provides the following functionalities:
 *
 * - Exit the platform's execution
 * - Start Modules
 * - Stop Modules
 * - Pause / Resume Modules
 */
public class ModuleController extends UtilityModuleObject implements PlatformOutput {

    /**
     * Creates a new Module controller output.
     *
     * @param module Owner module of this output
     * @param name   Name of this output. Normally hardcoded as "MODULE_NAME.control"
     */
    public ModuleController(ImmutablePlatformModule module, String name) {
        super(module,name);
    }


    /**
     * Based on processed responses from the inputs executes the different functionalities
     * @param response Must be in a way: "util COMMAND" and additionally:
     *                 - The name of the module (following the commands "startModule", "pauseModule", "resumeModule" and "stopModule")
     * @throws ModuleNotFoundException If the module which is aimed to be controlled is not configured
     * @throws ModuleControllerException If the command passed is invalid or incomplete
     */
    @Override
    public void execute(String response) throws ModuleNotFoundException, ModuleControllerException {
        this.getLogger().debug(this.getName(),"Util execute, response: "+response);
        String[] command = response.split(" ");
        if (command[0].equals("util")) {
            this.getLogger().debug(this.getName(),command[1] +" command received");
            //this.getLogger().debug(this.getName()+" - "+command[2] +" additional command received");
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
