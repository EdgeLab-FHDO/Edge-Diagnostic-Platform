package InfrastructureManager.Modules.MatchMaking;

import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;
import InfrastructureManager.ModuleManagement.ModuleDebugInput;

public abstract class MatchMakingLoggableElement {

    private final ImmutablePlatformModule ownerModule;

    public MatchMakingLoggableElement(ImmutablePlatformModule ownerModule) {
        this.ownerModule = ownerModule;
    }
    public ModuleDebugInput getLogger() {
        return this.ownerModule.getDebugInput();
    }
}
