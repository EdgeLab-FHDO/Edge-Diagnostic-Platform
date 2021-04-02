package InfrastructureManager.Modules.CustomConnector;

import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;
import InfrastructureManager.PlatformObject;

/**
 * Concrete implementation of {@link PlatformObject} for the CustomConnector Module.
 *
 * All objects (input and outputs included) of the module must extend this class.
 */
public class CustomConnectorModuleObject extends PlatformObject {
    /**
     * Creates a new CustomConnectorModuleObject
     * @param ownerModule Owner module of this object
     * @param name Name of the object
     */
    public CustomConnectorModuleObject(ImmutablePlatformModule ownerModule, String name) {
        super(ownerModule, name);
    }
}