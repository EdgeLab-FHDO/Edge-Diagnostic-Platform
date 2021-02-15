package InfrastructureManager.ModuleManagement;

import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleExecutionException;

public interface PlatformOutput{
    void execute(String response) throws ModuleExecutionException;
    String getName();
    PlatformModule.ModuleState getOwnerModuleState();
}
