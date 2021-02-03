package InfrastructureManager.ModuleManagement;

import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;
import InfrastructureManager.ModuleManagement.ModuleDebugInput;

public abstract class LoggableObject {

    private final ImmutablePlatformModule ownerModule;

    public LoggableObject(ImmutablePlatformModule ownerModule) {
        this.ownerModule = ownerModule;
    }

    protected ModuleDebugInput getLogger() {
        return this.ownerModule.getDebugInput();
    }
}
