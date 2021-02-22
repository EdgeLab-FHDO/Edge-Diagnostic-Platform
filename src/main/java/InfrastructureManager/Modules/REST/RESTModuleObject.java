package InfrastructureManager.Modules.REST;

import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;
import InfrastructureManager.PlatformObject;

/**
 * Concrete implementation of {@link PlatformObject} for the REST Module.
 * <p>
 * All objects (input and outputs included) of the module must extend this class.
 */
public class RESTModuleObject extends PlatformObject {
    /**
     * Constructor of the class. Creates a new Rest module object.
     *
     * @param ownerModule Owner module of this object
     */
    public RESTModuleObject(ImmutablePlatformModule ownerModule) {
        super(ownerModule);
    }

    /**
     * Constructor of the class. Creates a new named Rest module object.
     *
     * @param ownerModule Owner module of this object
     * @param name        Name of this object
     */
    public RESTModuleObject(ImmutablePlatformModule ownerModule, String name) {
        super(ownerModule, name);
    }
}
