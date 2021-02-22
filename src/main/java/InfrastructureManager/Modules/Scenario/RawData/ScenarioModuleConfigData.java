package InfrastructureManager.Modules.Scenario.RawData;

import InfrastructureManager.ModuleManagement.ModuleFactory.ModuleType;
import InfrastructureManager.ModuleManagement.RawData.ModuleConfigData;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Raw data representing a ScenarioModule.
 * <p>
 * To create a scenario module the name and the path to the scenario file are needed.
 * Type is automatically selected as {@link ModuleType}.SCENARIO
 */
public class ScenarioModuleConfigData extends ModuleConfigData {

    private final String path;

    /**
     * Creates a new raw console module. Uses the {@link JsonProperty} annotation to extract information from
     * "name" and "path" fields when deserializing.
     * @param name Name of the module
     * @param path Path of the scenario file
     */
    public ScenarioModuleConfigData(@JsonProperty("name") String name, @JsonProperty("path") String path) {
        super(name);
        this.type = ModuleType.SCENARIO;
        this.path = path;
    }

    /**
     * Returns the scenario path configured in this raw module.
     *
     * @return Scenario Path
     */
    public String getPath() {
        return path;
    }
}
