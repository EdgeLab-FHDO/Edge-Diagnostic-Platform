package InfrastructureManager.Modules.Utility.RawData;

import InfrastructureManager.ModuleManagement.ModuleFactory.ModuleType;
import InfrastructureManager.ModuleManagement.RawData.ModuleConfigData;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Raw data representing a UtilityModule.
 *
 * To create a utility module only the name is needed.
 * Type is automatically selected as {@link ModuleType}.UTILITY
 */
public class UtilityModuleConfigData extends ModuleConfigData {

    /**
     * Creates a new raw utility module. Uses the {@link JsonProperty} annotation for extracting the
     * name while deserializing.
     * @param name Name of the module
     */
    public UtilityModuleConfigData(@JsonProperty("name") String name) {
        super(name);
        this.type = ModuleType.UTILITY;
    }
}
