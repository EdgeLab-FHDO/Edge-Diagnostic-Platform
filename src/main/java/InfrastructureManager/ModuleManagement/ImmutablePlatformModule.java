package InfrastructureManager.ModuleManagement;

import java.util.List;
import java.util.Map;

/**
 * Interface for the different modules to implement to appear as read-only immutable objects
 */
public interface ImmutablePlatformModule {

    /**
     * Return the inputs of the module as a list
     * @return List of Inputs of the current module
     */
    List<PlatformInput> getInputs();

    /**
     * Return the outputs of the module as a list
     * @return List of Outputs of the current module
     */
    List<PlatformOutput> getOutputs();

    /**
     * Return the name of the current module (as configured initially)
     * @return Name of the current module
     */
    String getName();

    /**
     * Returns the state of the current module. Can be any valid value of the {@link InfrastructureManager.ModuleManagement.PlatformModule.ModuleState}
     * enum.
     * @return State of the module
     */
    PlatformModule.ModuleState getState();

    /**
     * Returns the connections that each platform input has inside the module.
     * @return A Map where the key is the input's name and the value is a list of Connection objects
     */
    Map<String, List<Connection>> getInputConnections();

    /**
     * Gives access to the built-in debug input of each module
     * @return a ModuleDebugInput object which allows to debug code inside the module and is read like any other input
     */
    ModuleDebugInput getDebugInput();
}
