package InfrastructureManager.ModuleManagement;

import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleExecutionException;
import InfrastructureManager.ModuleManagement.Exception.Execution.ModulePausedException;
import InfrastructureManager.ModuleManagement.PlatformModule.ModuleState;

public abstract class ModuleOutput {
    private final String name;
    private ModuleState moduleState;

    public ModuleOutput(String name) {
        this.name = name;
    }

    public void reportState(ModuleState state) {
        this.moduleState = state;
    }


    public String getName() {
        return name;
    }

    public void write(String response) throws IllegalArgumentException, ModuleExecutionException {
        if (this.moduleState == ModuleState.PAUSED) {
            throw new ModulePausedException("Cannot write to output, Module is paused");
        }
        out(response);
    }

    protected abstract void out (String response) throws ModuleExecutionException;
}
