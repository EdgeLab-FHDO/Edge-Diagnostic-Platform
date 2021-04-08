package InfrastructureManager.Modules.Diagnostics.Input;

import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleExecutionException;
import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;
import InfrastructureManager.ModuleManagement.PlatformInput;
import InfrastructureManager.Modules.Diagnostics.DiagnosticsModuleObject;

public abstract class AbstractInstructionInput extends DiagnosticsModuleObject implements PlatformInput {

    private String toSend;

    public AbstractInstructionInput(ImmutablePlatformModule ownerModule, String name) {
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

    protected abstract String getInstruction() throws InterruptedException;

    @Override
    public void response(ModuleExecutionException outputException) {}
}
