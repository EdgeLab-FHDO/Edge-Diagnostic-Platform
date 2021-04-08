package InfrastructureManager.Modules.Diagnostics;

import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;
import InfrastructureManager.Modules.Diagnostics.RawData.Instruction.ApplicationInstruction;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class InstructionSet extends DiagnosticsModuleObject {

    private final BlockingQueue<ApplicationInstruction> appInstructions;
    private final BlockingQueue<String> aeInstructions;
    private final BlockingQueue<String> computeInstructions;

    public InstructionSet(ImmutablePlatformModule ownerModule) {
        super(ownerModule);
        this.appInstructions = new ArrayBlockingQueue<>(10);
        this.aeInstructions = new ArrayBlockingQueue<>(10);
        this.computeInstructions = new ArrayBlockingQueue<>(10);
    }

    public void addAppInstruction(ApplicationInstruction instruction) throws InterruptedException {
        appInstructions.put(instruction);
    }

    public ApplicationInstruction getAppInstruction() throws InterruptedException {
        return appInstructions.take();
    }

    public void addAEInstruction(String instruction) throws InterruptedException {
        aeInstructions.put(instruction);
    }

    public String getAEInstruction() throws InterruptedException {
        return aeInstructions.take();
    }

    public void addComputeInstruction(String instruction) throws InterruptedException {
        computeInstructions.put(instruction);
    }

    public String getComputeInstruction() throws InterruptedException {
        return computeInstructions.take();
    }
}
