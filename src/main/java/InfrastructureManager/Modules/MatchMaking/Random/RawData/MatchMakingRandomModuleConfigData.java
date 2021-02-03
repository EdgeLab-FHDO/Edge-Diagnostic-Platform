package InfrastructureManager.Modules.MatchMaking.Random.RawData;

import InfrastructureManager.ModuleManagement.ModuleFactory.ModuleType;
import InfrastructureManager.ModuleManagement.RawData.ModuleConfigData;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MatchMakingRandomModuleConfigData extends ModuleConfigData {

    public MatchMakingRandomModuleConfigData(@JsonProperty("name") String name) {
        super(name);
        this.type = ModuleType.MATCH_MAKING_RANDOM;
    }
}
