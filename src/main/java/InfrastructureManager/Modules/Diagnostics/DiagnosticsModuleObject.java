package InfrastructureManager.Modules.Diagnostics;

import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;
import InfrastructureManager.PlatformObject;

public class DiagnosticsModuleObject extends PlatformObject {
    public DiagnosticsModuleObject(ImmutablePlatformModule ownerModule) {
        super(ownerModule);
    }

    public DiagnosticsModuleObject(ImmutablePlatformModule ownerModule, String name) {
        super(ownerModule, name);
    }

    public InstructionList getInstructionList() {
        GlobalVarAccessDiagnosticsModule castedModule = (GlobalVarAccessDiagnosticsModule) this.getOwnerModule();
        return castedModule.getInstructionList();
    }
}
