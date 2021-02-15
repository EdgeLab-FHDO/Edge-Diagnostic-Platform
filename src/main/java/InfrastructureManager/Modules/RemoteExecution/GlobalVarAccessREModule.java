package InfrastructureManager.Modules.RemoteExecution;

import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;

interface GlobalVarAccessREModule extends ImmutablePlatformModule {
    LimitList getLimitList();
}
