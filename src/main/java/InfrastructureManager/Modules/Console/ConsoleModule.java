package InfrastructureManager.Modules.Console;


import InfrastructureManager.ModuleManagement.PlatformModule;

public class ConsoleModule extends PlatformModule {


    public ConsoleModule(String name) {
        super(name);
        setInputs(new ConsoleInput(name + ".in"));
        setOutputs(new ConsoleOutput(name + ".out"));
    }
}