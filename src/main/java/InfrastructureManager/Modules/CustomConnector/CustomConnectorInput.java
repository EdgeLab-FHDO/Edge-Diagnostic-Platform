package InfrastructureManager.Modules.CustomConnector;

import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleExecutionException;
import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;
import InfrastructureManager.ModuleManagement.PlatformInput;

import java.util.Scanner;

public class CustomConnectorInput extends CustomConnectorModuleObject implements PlatformInput {
    private final Scanner IN = new Scanner(System.in);

    /**
     * Creates a new CustomConnectorInput
     * @param module Owner module of this input
     * @param name Name of this input
     */
    public CustomConnectorInput(ImmutablePlatformModule module, String name) {
        super(module,name);
    }

    @Override
    public String read() {
        System.out.println("Input1 >");
        String reading = IN.nextLine();
        this.getLogger().debug(reading);
        return reading;
    }

    /**
     * Handles exceptions in the execution process. In this particular input, the exception message is printed to the console.
     * @param outputException Exception happening in the platform's execution process. This value is null if the process went correctly (No exception was raised)
     */
    @Override
    public void response(ModuleExecutionException outputException) {
        if (outputException != null) {
            outputException.printStackTrace();
        }
    }
}


