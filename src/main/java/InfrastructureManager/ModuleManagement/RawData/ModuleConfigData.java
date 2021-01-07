package InfrastructureManager.ModuleManagement.RawData;

import InfrastructureManager.ModuleManagement.ModuleType;
import InfrastructureManager.ModuleManagement.RawData.Modules.ConsoleModuleConfigData;
import InfrastructureManager.ModuleManagement.RawData.Modules.UtilityModuleConfigData;
import com.fasterxml.jackson.annotation.*;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", defaultImpl = ModuleConfigData.class)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ConsoleModuleConfigData.class, name = "ConsoleModule"),
        @JsonSubTypes.Type(value = UtilityModuleConfigData.class, name = "UtilityModule")
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
