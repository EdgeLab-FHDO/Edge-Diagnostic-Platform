package InfrastructureManager.Modules.MatchMaking;

import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;

public interface GlobalVarAccessMMModule extends ImmutablePlatformModule {

    MatchesList getSharedList();
}
