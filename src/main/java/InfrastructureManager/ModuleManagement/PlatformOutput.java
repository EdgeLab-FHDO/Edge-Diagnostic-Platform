package InfrastructureManager.ModuleManagement;

import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleExecutionException;
import InfrastructureManager.ModuleManagement.PlatformModule.ModuleState;
import InfrastructureManager.PlatformObject;

public abstract class PlatformOutput extends PlatformObject {
    private final String name;

    public PlatformOutput(ImmutablePlatformModule ownerModule, String name) {
        super(ownerModule);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public ModuleState getOwnerModuleState() {
        return this.getOwnerModule().getState();
    }

    public abstract void execute(String response) throws ModuleExecutionException;
}
