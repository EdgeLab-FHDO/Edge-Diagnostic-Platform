package InfrastructureManager.Modules.Utility;

import InfrastructureManager.ModuleManagement.PlatformModule;

public class UtilityModule extends PlatformModule {
    public UtilityModule(String name) {
        super(name);
        setInputs(); //NO INPUTS
        setOutputs(new MasterUtility(name + ".out"));
    }
}
