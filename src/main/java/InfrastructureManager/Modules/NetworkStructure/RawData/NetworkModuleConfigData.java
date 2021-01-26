package InfrastructureManager.Modules.NetworkStructure.RawData;

import InfrastructureManager.ModuleManagement.ModuleFactory.ModuleType;
import InfrastructureManager.ModuleManagement.RawData.ModuleConfigData;
import com.fasterxml.jackson.annotation.JsonProperty;

public class NetworkModuleConfigData extends ModuleConfigData {


    public NetworkModuleConfigData(@JsonProperty("name") String name) {
        super(name);
        this.type = ModuleType.NETWORK_STRUCTURE;
    }
}
