package InfrastructureManager.Modules.Diagnostics;

import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;

public class InstructionLock extends DiagnosticsModuleObject{

    private boolean next;

    public InstructionLock(ImmutablePlatformModule ownerModule) {
        super(ownerModule);
        next = false;
    }

    public synchronized void unlock() {
        next = true;
        notifyAll();
    }

    public synchronized void waitOnNext() {
        while (!next) {
            try {
                wait();
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
            }
        }
        next = false;
    }
}
