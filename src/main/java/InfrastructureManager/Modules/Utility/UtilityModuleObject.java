package InfrastructureManager.Modules.Utility;

import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;
import InfrastructureManager.PlatformObject;

/**
 * Concrete implementation of {@link PlatformObject} for the Utility Module.
 *
 * All objects (input and outputs included) of the module must extend this class.
 */
public class UtilityModuleObject extends PlatformObject {

    /**
     * Creates a new UtilityModuleObject.
     *
     * @param ownerModule Owner module of this object
     * @param name        Name of this object
     */
    public UtilityModuleObject(ImmutablePlatformModule ownerModule, String name) {
        super(ownerModule, name);
    }
}
