package InfrastructureManager.Modules.Utility;

import InfrastructureManager.ModuleManagement.PlatformModule;
import InfrastructureManager.ModuleManagement.RawData.ModuleConfigData;

public class UtilityModule extends PlatformModule {
    public UtilityModule() {
        super();
    }

    @Override
    public void configure(ModuleConfigData data) {
        this.setName(data.getName());
        setInputs(); //NO INPUTS
        setOutputs(new MasterUtility(this.getName() + ".internal"),
                new FileOutput(this.getName() + ".fileOut"));
    }
}
