package InfrastructureManager;

import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;
import InfrastructureManager.ModuleManagement.ModuleDebugInput;
import InfrastructureManager.ModuleManagement.PlatformModule;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Base class for objects of the platform. All modules should have a specific implementation that
 * must extend this class. In turn, all objects on the module should extend it, in order to have access to its module's {@link ModuleDebugInput} and
 * be able to log information.
 *
 * It also provides access to the ownerModule of each object, using its {@link ImmutablePlatformModule} interface,
 */
@JsonIgnoreProperties({"ownerModule", "name","ownerModuleState", "logger"})
public abstract class PlatformObject {

    private final ImmutablePlatformModule ownerModule;
    private final String name;
    /**
     * Overloaded constructor of the class. Creates an objet with a name field "" (Empty)
     * @param ownerModule The module which to which this object belongs
     */
    public PlatformObject(ImmutablePlatformModule ownerModule) {
        this(ownerModule,"");
    }

    /**
     * Constructor of the class. Creates a new PlatformObject based on its ownerModule and a name
     * @param ownerModule Module that owns this object
     * @param name Name for this object
     */
    public PlatformObject(ImmutablePlatformModule ownerModule, String name) {
        this.ownerModule = ownerModule;
        this.name = name;
    }

    /**
     * Gives the name given for this object
     * @return Name of this object. Returns "" if this object was created with the {@link #PlatformObject(ImmutablePlatformModule)} constructor
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the current state of the owner module of this object. This can be any valid {@link InfrastructureManager.ModuleManagement.PlatformModule.ModuleState}
     * @return Current state of the owner module of this object.
     */
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
