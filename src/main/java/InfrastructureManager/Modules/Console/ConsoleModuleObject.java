package InfrastructureManager.Modules.Console;

import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;
import InfrastructureManager.PlatformObject;

/**
 * Concrete implementation of {@link PlatformObject} for the Console Module.
 *
 * All objects (input and outputs included) of the module must extend this class.
 */
public class ConsoleModuleObject extends PlatformObject {

    /**
     * Creates a new ConsoleModuleObject
     * @param ownerModule Owner module of this object
     * @param name Name of the object
     */
    public ConsoleModuleObject(ImmutablePlatformModule ownerModule, String name) {
        super(ownerModule, name);
    }
}
