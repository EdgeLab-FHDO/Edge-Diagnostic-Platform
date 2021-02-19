package InfrastructureManager.Modules.RemoteExecution;

import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;

/**
 * This interface provides a standard, well-defined manner of
 * giving objects inside the module (in this case the Remote execution module) access to a shared resource.
 *
 * In this particular case, provides access to the shared {@link LimitList} used between {@link InfrastructureManager.Modules.RemoteExecution.Input.NodeLimitInput} and
 * {@link InfrastructureManager.Modules.RemoteExecution.Output.NodeLimitOutput} instances to communicate.
 *
 * Having a separate interface adds a level of "information hiding" and allows objects to only access global variables in the module
 * without being able to alter the state of the module itself.
 */
interface GlobalVarAccessREModule extends ImmutablePlatformModule {

    /**
     * Method to be implemented by the module to provide the shared list
     *
     * @return the shared limit list
     */
    LimitList getLimitList();
}
