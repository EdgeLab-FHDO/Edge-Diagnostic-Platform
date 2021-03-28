package InfrastructureManager.Modules.Diagnostics;

import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;
import InfrastructureManager.Modules.Diagnostics.RawData.Instruction.Instruction;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class InstructionList extends DiagnosticsModuleObject {

    private final BlockingQueue<Instruction> instructions;

    public InstructionList(ImmutablePlatformModule ownerModule) {
        super(ownerModule);
        this.instructions = new ArrayBlockingQueue<>(10);
    }

    public void addInstruction(Instruction instruction) throws InterruptedException {
        instructions.put(instruction);
    }

    public Instruction getInstruction() throws InterruptedException {
        return instructions.take();
    }
}
