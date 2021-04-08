package InfrastructureManager.Modules.Diagnostics.Input;

import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;
import InfrastructureManager.Modules.Diagnostics.RawData.Instruction.ApplicationInstruction;

public class ApplicationInstructionInput extends AbstractInstructionInput {

    public ApplicationInstructionInput(ImmutablePlatformModule ownerModule, String name) {
        super(ownerModule, name);
    }

    protected String getInstruction() throws InterruptedException {
        ApplicationInstruction instruction = this.getInstructionList().getAppInstruction();
        return instruction.getClientInstruction() + " " + instruction.getServerInstruction();
    }

}
