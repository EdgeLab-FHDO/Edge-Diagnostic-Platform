package InfrastructureManager.Modules.Console;

import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;
import InfrastructureManager.PlatformObject;

public class ConsoleModuleObject extends PlatformObject {
    public ConsoleModuleObject(ImmutablePlatformModule ownerModule) {
        super(ownerModule);
    }

    public ConsoleModuleObject(ImmutablePlatformModule ownerModule, String name) {
        super(ownerModule, name);
    }
}
