package InfrastructureManager.Modules.AdvantEDGE;

import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;
import InfrastructureManager.PlatformObject;

/**
 * Concrete implementation of {@link PlatformObject} for the AdvantEdge Module.
 *
 * All objects (input and outputs included) of the module must extend this class.
 */
public class AdvantEdgeModuleObject extends PlatformObject {

    /**
     * Creates a new AdvantEdgeModuleObject
     * @param ownerModule Owner module of this object
     * @param name Name of this object
     */
    public AdvantEdgeModuleObject(ImmutablePlatformModule ownerModule, String name) {
        super(ownerModule, name);
    }
}
