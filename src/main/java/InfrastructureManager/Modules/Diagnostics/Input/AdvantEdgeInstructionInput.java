package InfrastructureManager.Modules.Diagnostics.Input;

import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;
import InfrastructureManager.Modules.Diagnostics.RawData.Instruction.ApplicationInstruction;

public class AdvantEdgeInstructionInput extends AbstractInstructionInput {

    public AdvantEdgeInstructionInput(ImmutablePlatformModule ownerModule, String name) {
        super(ownerModule, name);
    }

    protected String getInstruction() throws InterruptedException {
        return this.getInstructionList().getAEInstruction();
    }

}
