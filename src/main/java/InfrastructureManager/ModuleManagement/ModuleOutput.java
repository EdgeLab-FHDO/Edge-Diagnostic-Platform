package InfrastructureManager.ModuleManagement;

import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleExecutionException;

import com.fasterxml.jackson.core.JsonProcessingException;

import InfrastructureManager.ModuleManagement.PlatformModule.ModuleState;

public abstract class ModuleOutput {
    private final String name;
    private final PlatformModule ownerModule;

    public ModuleOutput(PlatformModule ownerModule, String name) {
        this.name = name;
        this.ownerModule = ownerModule;
    }

    public String getName() {
        return name;
    }

    public PlatformModule getOwnerModule() {
        return ownerModule;
    }

    public ModuleState getOwnerModuleState() {
        return this.ownerModule.getState();
    }

    public abstract void execute(String response) throws ModuleExecutionException;
}
