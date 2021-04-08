package InfrastructureManager.Modules.Diagnostics.Input;

import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;

public class ComputeInstructionInput extends AbstractInstructionInput {

    public ComputeInstructionInput(ImmutablePlatformModule ownerModule, String name) {
        super(ownerModule, name);
    }

    protected String getInstruction() throws InterruptedException {
        return this.getInstructionList().getComputeInstruction();
    }

}
