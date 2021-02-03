package InfrastructureManager.ModuleManagement;

import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleExecutionException;
import InfrastructureManager.ModuleManagement.ImmutablePlatformModule.ModuleState;

public abstract class PlatformOutput {
    private final String name;
    private final ImmutablePlatformModule ownerModule;

    public PlatformOutput(ImmutablePlatformModule ownerModule, String name) {
        this.name = name;
        this.ownerModule = ownerModule;
    }

    public String getName() {
        return name;
    }

    public ImmutablePlatformModule getOwnerModule() {
        return ownerModule;
    }

    public ModuleState getOwnerModuleState() {
        return this.ownerModule.getState();
    }

    public abstract void execute(String response) throws ModuleExecutionException;
}
