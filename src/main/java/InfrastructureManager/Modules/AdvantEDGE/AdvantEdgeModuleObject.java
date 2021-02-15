package InfrastructureManager.Modules.AdvantEDGE;

import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;
import InfrastructureManager.PlatformObject;

public class AdvantEdgeModuleObject extends PlatformObject {
    public AdvantEdgeModuleObject(ImmutablePlatformModule ownerModule) {
        super(ownerModule);
    }

    public AdvantEdgeModuleObject(ImmutablePlatformModule ownerModule, String name) {
        super(ownerModule, name);
    }
}
