package InfrastructureManager.Modules.MatchMaking;

import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;

interface GlobalVarAccessMMModule extends ImmutablePlatformModule {

    MatchesList getSharedList();
}
