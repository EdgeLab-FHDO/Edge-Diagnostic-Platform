package InfrastructureManager.Modules.Scenario;

import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;
import InfrastructureManager.PlatformObject;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Concrete implementation of {@link PlatformObject} for the Scenario Module.
 * <p>
 * All objects (input and outputs included) of the module must extend this class.
 * <p>
 * Additionally to access to the owner module's pointer and the built-in logger, this particular class
 * enables objects inside the Scenario Module to have access to the shared resource that is the Scenario defined
 * for the module.
 */
public class ScenarioModuleObject extends PlatformObject {
    /**
     *  Creates a new Scenario module object.
     *
     * @param ownerModule Owner module of this object
     */
    public ScenarioModuleObject(ImmutablePlatformModule ownerModule) {
        super(ownerModule);
    }

    /**
     * Creates a new named Scenario module object.
     *
     * @param ownerModule Owner module of this object
     * @param name        Name of this object
     */
    public ScenarioModuleObject(ImmutablePlatformModule ownerModule, String name) {
        super(ownerModule, name);
    }

    /**
     * Returns the Scenario assigned to the owner module
     *
     * The Scenario is accessed using the {@link GlobalVarAccessScenarioModule} interface of the owner Module which provides
     * more access that the default {@link ImmutablePlatformModule} interface does.
     *
     * @return Scenario of the owner module.
     */
    @JsonIgnore
    public Scenario getScenario() {
        GlobalVarAccessScenarioModule casted = (GlobalVarAccessScenarioModule) this.getOwnerModule();
        this.getLogger().debug(this.getName() +" - Scenario assigned to the owner module is: "+casted.getScenario().getName());
        return casted.getScenario();
    }
}
