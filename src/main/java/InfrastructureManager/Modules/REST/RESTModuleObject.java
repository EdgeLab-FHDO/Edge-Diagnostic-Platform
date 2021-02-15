package InfrastructureManager.Modules.REST;

import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;
import InfrastructureManager.PlatformObject;

public class RESTModuleObject extends PlatformObject {
    public RESTModuleObject(ImmutablePlatformModule ownerModule) {
        super(ownerModule);
    }

    public RESTModuleObject(ImmutablePlatformModule ownerModule, String name) {
        super(ownerModule, name);
    }
}
