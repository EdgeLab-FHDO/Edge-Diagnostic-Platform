package InfrastructureManager.Modules.Utility;

import InfrastructureManager.ModuleManagement.PlatformModule;

public class UtilityModule extends PlatformModule {
    public UtilityModule(String name) {
        super(name);
        setInputs();
        setOutputs(new MasterUtility(name + ".out"));
    }
}
