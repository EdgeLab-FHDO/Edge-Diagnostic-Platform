package InfrastructureManager;

import InfrastructureManager.ModuleManagement.GlobalVarAccessPlatformModule;
import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;
import InfrastructureManager.ModuleManagement.ModuleDebugInput;

/**
 * Base class for objects of the platform. All objects that are used in / are part of a module,
 * must extend this class, in order to have access to its module's {@link ModuleDebugInput} and
 * be able to log information.
 */
public abstract class PlatformObject {

    private final ImmutablePlatformModule ownerModule;

    /**
     * Constructor of the class.
     * @param ownerModule The module which to which this object belongs
     */
    public PlatformObject(ImmutablePlatformModule ownerModule) {
        this.ownerModule = ownerModule;
    }


    /**
     * Access the owner module of this object
     * @return An {@link ImmutablePlatformModule} object, that only allows read operations
     */
    protected ImmutablePlatformModule getOwnerModule() {
        return ownerModule;
    }


    protected GlobalVarAccessPlatformModule getOwnerModuleWithGlobalVar() {
        return (GlobalVarAccessPlatformModule) ownerModule;
    }

    /**
     * Access the {@link ModuleDebugInput} inside the owner module of this object. This can then be
     * used to log information on different levels (DEBUG, WARN, ERROR).
     * @return A ModuleDebugInput object corresponding to the debug input of this object's owner module
     */
    protected ModuleDebugInput getLogger() {
        return this.ownerModule.getDebugInput();
    }
}
