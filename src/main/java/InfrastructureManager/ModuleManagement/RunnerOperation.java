package InfrastructureManager.ModuleManagement;

import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleStoppedException;

@FunctionalInterface
public interface RunnerOperation {
    void process(Runner r, PlatformInput input) throws ModuleStoppedException;
}
