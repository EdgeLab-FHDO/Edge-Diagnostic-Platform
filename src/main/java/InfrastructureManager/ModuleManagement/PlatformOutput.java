package InfrastructureManager.ModuleManagement;

import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleExecutionException;

/**
 * Interface that defines an output for the Edge Diagnostics Platform.
 *
 * Outputs are components that allow the platform to interact with and control its
 * environment by triggering events of different nature.
 *
 * Classes that act as an output for the platform should not only implement this interface but also
 * extend the {@link InfrastructureManager.PlatformObject} class (or one of its module specific subclasses)
 */
public interface PlatformOutput{
    /**
     * Main method of a platform output. Receives a command and performs certain operation or triggers a certain event.
     * @param response Command as a String that triggers the action of the output
     * @throws ModuleExecutionException If an error occurs during execution.
     */
    void execute(String response) throws ModuleExecutionException;

    /**
     * Gives the name of this output. Normally is preceded by the owner module's name
     * @return Name of this output
     */
    String getName();

    /**
     * Returns the state of the owner module. Is important to avoid triggering outputs in paused modules
     * @return The state of the owner module of this output.
     */
    PlatformModule.ModuleState getOwnerModuleState();
}
