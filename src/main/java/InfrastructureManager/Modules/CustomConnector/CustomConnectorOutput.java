package InfrastructureManager.Modules.CustomConnector;

import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;
import InfrastructureManager.ModuleManagement.PlatformOutput;
import InfrastructureManager.Modules.CustomConnector.CustomConnectorModuleObject;
import InfrastructureManager.Modules.Console.Exception.ConsoleOutputException;

import java.util.Arrays;


public class CustomConnectorOutput extends CustomConnectorModuleObject implements PlatformOutput {

    /**
     * Creates a new CustomConnectorOutput
     * @param module Owner module of this output
     * @param name Name of this output
     */
    public CustomConnectorOutput(ImmutablePlatformModule module, String name) {
        super(module, name);
    }

    @Override
    public void execute(String response) throws ConsoleOutputException {
        String[] command = response.split(" ");
        if (command[0].equals("CustomConnector")) { //The commands must come like "CustomConnector command"
            try {
                String result = String.join(" ", Arrays.copyOfRange(command,1,command.length)); //Grab all arguments passed and space them
                System.out.println(result);
            } catch (IndexOutOfBoundsException e) {
                throw new ConsoleOutputException("Arguments missing for command" + response + " to CustomConnectorOutput");
            }
        }
    }



}
