package InfrastructureManager.ModuleManagement.RawData.Modules;

import InfrastructureManager.ModuleManagement.ModuleType;
import InfrastructureManager.ModuleManagement.RawData.ModuleConfigData;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ScenarioModuleConfigData extends ModuleConfigData {

    private final String path;

    public ScenarioModuleConfigData(@JsonProperty("name") String name, @JsonProperty("path") String path) {
        super(name);
        this.type = ModuleType.SCENARIO;
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
