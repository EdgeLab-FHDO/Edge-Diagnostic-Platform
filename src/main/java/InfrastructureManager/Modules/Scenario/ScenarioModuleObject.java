package InfrastructureManager.Modules.Scenario;

import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;
import InfrastructureManager.PlatformObject;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class ScenarioModuleObject extends PlatformObject {
    public ScenarioModuleObject(ImmutablePlatformModule ownerModule) {
        super(ownerModule);
    }

    public ScenarioModuleObject(ImmutablePlatformModule ownerModule, String name) {
        super(ownerModule, name);
    }

    @JsonIgnore
    public Scenario getScenario() {
        GlobalVarAccessScenarioModule casted = (GlobalVarAccessScenarioModule) this.getOwnerModule();
        return casted.getScenario();
    }
}
