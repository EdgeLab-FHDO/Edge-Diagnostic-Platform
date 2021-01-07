package InfrastructureManager.ModuleManagement.RawData;

import InfrastructureManager.Configuration.RawData.CustomCommandIO;
import InfrastructureManager.ModuleManagement.ModuleType;
import InfrastructureManager.ModuleManagement.RawData.Modules.ConsoleModuleConfigData;
import com.fasterxml.jackson.annotation.*;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", defaultImpl = ModuleConfigData.class)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ConsoleModuleConfigData.class, name = "ConsoleModule")
})
public abstract class ModuleConfigData {
    private String name;
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
