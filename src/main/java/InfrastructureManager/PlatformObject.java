package InfrastructureManager;

import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;
import InfrastructureManager.ModuleManagement.ModuleDebugInput;
import InfrastructureManager.ModuleManagement.PlatformModule;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Base class for objects of the platform. All objects that are used in / are part of a module,
 * must extend this class, in order to have access to its module's {@link ModuleDebugInput} and
 * be able to log information.
 */
@JsonIgnoreProperties({"ownerModule", "name","ownerModuleState", "logger"})
public abstract class PlatformObject {

    private final ImmutablePlatformModule ownerModule;
    private final String name;
    /**
     * Constructor of the class.
     * @param ownerModule The module which to which this object belongs
     */
    public PlatformObject(ImmutablePlatformModule ownerModule) {
        this(ownerModule,"");
    }

    public PlatformObject(ImmutablePlatformModule ownerModule, String name) {
        this.ownerModule = ownerModule;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public PlatformModule.ModuleState getOwnerModuleState() {
        return this.ownerModule.getState();
    }

    /**
     * Access the owner module of this object
     * @return An {@link ImmutablePlatformModule} object, that only allows read operations
     */
    protected ImmutablePlatformModule getOwnerModule() {
        return ownerModule;
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
