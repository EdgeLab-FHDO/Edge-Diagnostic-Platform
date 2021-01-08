package InfrastructureManager;

import InfrastructureManager.ModuleManagement.Exception.ModulePausedException;
import InfrastructureManager.ModuleManagement.PlatformModule.ModuleState;

public abstract class MasterOutput {
    private final String name;
    private ModuleState moduleState;

    public MasterOutput(String name) {
        this.name = name;
    }

    public void reportState(ModuleState state) {
        this.moduleState = state;
    }


    public String getName() {
        return name;
    }

    public void write(String response) throws Exception {
        if (this.moduleState == ModuleState.PAUSED) {
            throw new ModulePausedException("Cannot write to output, Module is paused");
        }
        out(response);
    }

    protected abstract void out (String response) throws Exception;
}
