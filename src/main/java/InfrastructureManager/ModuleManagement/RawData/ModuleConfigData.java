package InfrastructureManager.ModuleManagement.RawData;

import InfrastructureManager.ModuleManagement.ModuleFactory.*;
import InfrastructureManager.Modules.AdvantEDGE.RawData.AdvantEdgeModuleConfigData;
import InfrastructureManager.Modules.Console.RawData.ConsoleModuleConfigData;
import InfrastructureManager.Modules.REST.RawData.RESTModuleConfigData;
import InfrastructureManager.Modules.Scenario.RawData.ScenarioModuleConfigData;
import InfrastructureManager.Modules.Utility.RawData.UtilityModuleConfigData;
import com.fasterxml.jackson.annotation.*;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", defaultImpl = ModuleConfigData.class)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ConsoleModuleConfigData.class, name = "ConsoleModule"),
        @JsonSubTypes.Type(value = UtilityModuleConfigData.class, name = "UtilityModule"),
        @JsonSubTypes.Type(value = ScenarioModuleConfigData.class, name = "ScenarioModule"),
        @JsonSubTypes.Type(value = RESTModuleConfigData.class, name = "RESTModule"),
        @JsonSubTypes.Type(value = AdvantEdgeModuleConfigData.class, name = "AdvantEdgeModule")
})
public abstract class ModuleConfigData {
    private final String name;
    @JsonIgnore
    protected ModuleType type;

    @JsonCreator
    public ModuleConfigData(@JsonProperty("name") String name) {
        this.name = name;
        this.type = ModuleType.DEFAULT;
    }

    public String getName() {
        return name;
    }

    public ModuleType getType() {
        return type;
    }
}
