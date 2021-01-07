package InfrastructureManager.ModuleManagement.RawData.Modules;

import InfrastructureManager.ModuleManagement.ModuleType;
import InfrastructureManager.ModuleManagement.RawData.ModuleConfigData;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UtilityModuleConfigData extends ModuleConfigData {

    public UtilityModuleConfigData(@JsonProperty("name") String name) {
        super(name);
        this.type = ModuleType.UTILITY;
    }
}
