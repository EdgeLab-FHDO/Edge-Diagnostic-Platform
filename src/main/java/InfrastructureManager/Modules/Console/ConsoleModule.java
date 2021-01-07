package InfrastructureManager.Modules.Console;


import InfrastructureManager.ConsoleInput;
import InfrastructureManager.ConsoleOutput;
import InfrastructureManager.MasterInput;
import InfrastructureManager.MasterOutput;
import InfrastructureManager.ModuleManagement.PlatformModule;

public class ConsoleModule extends PlatformModule {


    public ConsoleModule(String name) {
        super(name, null, null);
        this.inputs = new MasterInput[]{new ConsoleInput("in-" + name)};
        this.outputs = new MasterOutput[]{new ConsoleOutput("out-" + name)};
    }
}
