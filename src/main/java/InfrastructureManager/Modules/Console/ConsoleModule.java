package InfrastructureManager.Modules.Console;


import InfrastructureManager.ModuleManagement.PlatformModule;
import InfrastructureManager.ModuleManagement.RawData.ModuleConfigData;

/**
 * {@link PlatformModule} that adds user interacting functionality to the platform.
 *
 * The console module, provides the necessary inputs and outputs to interact with the console (terminal),
 * which means read from standard input and print to the standard output.
 *
 * Provides one console input hardcoded with the name ".in"
 * And one console output with the name ".out"
 *
 * @see <a href="https://github.com/EdgeLab-FHDO/Edge-Diagnostic-Platform/wiki/Console-Module">Wiki Entry</a>
 */
public class ConsoleModule extends PlatformModule {


    /**
     * Create a new console module. The module created is not initialized and cannot be used
     */
    public ConsoleModule() {
        super();
    }

    /**
     * Based on raw module data, configures the module to use it. In this case, extracts the name and creates the input and
     * output with their predefined names.
     * @param data Raw module data.
     */
    @Override
    public void configure(ModuleConfigData data) {
        this.setName(data.getName());
        setInputs(new ConsoleInput(this, this.getName() + ".in"));
        setOutputs(new ConsoleOutput(this,this.getName() + ".out"));
    }
}
