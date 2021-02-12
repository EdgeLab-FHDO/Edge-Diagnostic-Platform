package InfrastructureManager.Modules.RemoteExecution;

import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;

public interface GlobalVarAccessREModule extends ImmutablePlatformModule {
    public LimitList getLimitList();
}
