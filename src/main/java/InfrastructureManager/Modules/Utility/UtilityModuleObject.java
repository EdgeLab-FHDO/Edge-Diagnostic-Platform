package InfrastructureManager.Modules.Utility;

import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;
import InfrastructureManager.PlatformObject;

public class UtilityModuleObject extends PlatformObject {
    public UtilityModuleObject(ImmutablePlatformModule ownerModule) {
        super(ownerModule);
    }

    public UtilityModuleObject(ImmutablePlatformModule ownerModule, String name) {
        super(ownerModule, name);
    }
}
