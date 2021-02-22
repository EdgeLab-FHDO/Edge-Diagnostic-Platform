package InfrastructureManager.ModuleManagement;

import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleStoppedException;

/**
 * Functional interface to represent an operation to be passed to a {@link Runner} to perform inside a loop.
 *
 * All implementations should implement the process method, which based on the runner and the input determines how to process
 * incoming commands or in general, what to do inside the runner's execution.
 */
@FunctionalInterface
public interface RunnerOperation {

    /**
     * Method to be implemented in this functional interface. Determines the behavior of a runner.
     * @param r Runner (normally the owner of the operation). It provides access to connections for example
     * @param input PlatformInput (Normally the input related to the owner runner)
     * @throws ModuleStoppedException If the module is stopped externally while this operation is running. This exception is used for exiting in a safe manner.
     */
    void process(Runner r, PlatformInput input) throws ModuleStoppedException;
}
