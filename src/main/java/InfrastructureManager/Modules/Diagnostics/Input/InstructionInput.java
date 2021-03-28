package InfrastructureManager.Modules.Diagnostics.Input;

import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleExecutionException;
import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;
import InfrastructureManager.ModuleManagement.PlatformInput;
import InfrastructureManager.Modules.Diagnostics.DiagnosticsModuleObject;
import InfrastructureManager.Modules.Diagnostics.RawData.Instruction.Instruction;

public class InstructionInput extends DiagnosticsModuleObject implements PlatformInput {

    private String toSend;

    public InstructionInput(ImmutablePlatformModule ownerModule, String name) {
        super(ownerModule, name);
        this.toSend = null;
    }

    @Override
    public String read() throws InterruptedException {
        toSend = "set_instruction " + getInstruction();
        String aux = toSend;
        toSend = "";
        return aux;
    }

    private String getInstruction() throws InterruptedException {
        Instruction instruction = this.getInstructionList().getInstruction();
        return instruction.getClientInstruction() + " " + instruction.getServerInstruction();
    }

    @Override
    public void response(ModuleExecutionException outputException) {}
}
