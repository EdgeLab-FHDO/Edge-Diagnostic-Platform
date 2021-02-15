package InfrastructureManager.Modules.MatchMaking;

import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;
import InfrastructureManager.PlatformObject;

public class MatchMakingModuleObject extends PlatformObject {
    public MatchMakingModuleObject(ImmutablePlatformModule ownerModule) {
        super(ownerModule);
    }

    public MatchMakingModuleObject(ImmutablePlatformModule ownerModule, String name) {
        super(ownerModule, name);
    }

    public MatchesList getSharedList() {
        GlobalVarAccessMMModule casted = (GlobalVarAccessMMModule)this.getOwnerModule();
        return casted.getSharedList();
    }
}
