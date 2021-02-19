package InfrastructureManager.Modules.RemoteExecution;

import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;
import InfrastructureManager.PlatformObject;

/**
 * Concrete implementation of {@link PlatformObject} for the Remote Execution Module.
 *
 * All objects (input and outputs included) of the module must extend this class.
 *
 * Additionally to access to the owner module's pointer and the built-in logger, this particular class
 * enables objects inside the Remote Execution Module to have access to the shared Limit lists resource
 *
 * @see LimitList
 */
public class RemoteExecutionModuleObject extends PlatformObject {
    /**
     * Creates a new RemoteExecutionModuleObject.
     *
     * @param ownerModule Owner module of this object
     */
    public RemoteExecutionModuleObject(ImmutablePlatformModule ownerModule) {
        super(ownerModule);
    }

    /**
     * Creates a new RemoteExecutionModuleObject.
     *
     * @param ownerModule Owner module of this object
     * @param name        Name of this object
     */
    public RemoteExecutionModuleObject(ImmutablePlatformModule ownerModule, String name) {
        super(ownerModule, name);
    }

    /**
     * Returns the shared limit list.
     *
     * The list is accessed using the {@link GlobalVarAccessREModule} interface of the owner Module which provides
     * more access that the default {@link ImmutablePlatformModule} interface does.
     *
     * @return List that contains all created limits and is used for interaction between {@link InfrastructureManager.Modules.RemoteExecution.Input.NodeLimitInput} and
     * {@link InfrastructureManager.Modules.RemoteExecution.Output.NodeLimitOutput} instances.
     */
    public LimitList getLimitList() {
        GlobalVarAccessREModule castedModule = (GlobalVarAccessREModule) this.getOwnerModule();
        return castedModule.getLimitList();
    }
}
