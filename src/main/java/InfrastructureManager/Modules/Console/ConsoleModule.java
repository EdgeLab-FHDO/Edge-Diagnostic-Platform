package InfrastructureManager.Modules.Console;


import InfrastructureManager.ModuleManagement.PlatformModule;
import InfrastructureManager.ModuleManagement.RawData.ModuleConfigData;

public class ConsoleModule extends PlatformModule {


    public ConsoleModule() {
        super();
    }

    @Override
    public void configure(ModuleConfigData data) {
        this.setName(data.getName());
        setInputs(new ConsoleInput(this, this.getName() + ".in"));
        setOutputs(new ConsoleOutput(this,this.getName() + ".out"));
    }
}
