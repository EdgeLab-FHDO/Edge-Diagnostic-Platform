package InfrastructureManager.Modules.RemoteExecution;

import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;
import InfrastructureManager.PlatformObject;

public class RemoteExecutionModuleObject extends PlatformObject {
    public RemoteExecutionModuleObject(ImmutablePlatformModule ownerModule) {
        super(ownerModule);
    }

    public RemoteExecutionModuleObject(ImmutablePlatformModule ownerModule, String name) {
        super(ownerModule, name);
    }

    public LimitList getLimitList() {
        GlobalVarAccessREModule castedModule = (GlobalVarAccessREModule) this.getOwnerModule();
        return castedModule.getLimitList();
    }
}
