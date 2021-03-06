package InfrastructureManager.Modules.MatchMaking;

import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;
import InfrastructureManager.PlatformObject;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class MatchMakingModuleObject extends PlatformObject {
    public MatchMakingModuleObject(ImmutablePlatformModule ownerModule) {
        super(ownerModule);
    }

    public MatchMakingModuleObject(ImmutablePlatformModule ownerModule, String name) {
        super(ownerModule, name);
    }

    @JsonIgnore
    public MatchesList getSharedList() {
        GlobalVarAccessMMModule casted = (GlobalVarAccessMMModule)this.getOwnerModule();
        return casted.getSharedList();
    }
}
