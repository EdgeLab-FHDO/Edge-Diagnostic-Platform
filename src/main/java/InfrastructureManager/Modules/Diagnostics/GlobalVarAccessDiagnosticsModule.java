package InfrastructureManager.Modules.Diagnostics;

import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;

/**
 * This interface provides a standard, well-defined manner of
 * giving objects inside the module (in this case the Diagnostics module) access to a shared resource.
 *
 * In this particular case, it provides access to a shared {@link InstructionSet} used between inputs and
 * outputs to communicate.
 *
 * Having a separate interface adds a level of "information hiding" and allows objects to only access global variables in the module
 * without being able to alter the state of the module itself.
 */
public interface GlobalVarAccessDiagnosticsModule extends ImmutablePlatformModule {

    /**
     * Method to be implemented by the module to provide the shared instruction list
     * @return The shared list
     */
    InstructionSet getInstructionList();
}
