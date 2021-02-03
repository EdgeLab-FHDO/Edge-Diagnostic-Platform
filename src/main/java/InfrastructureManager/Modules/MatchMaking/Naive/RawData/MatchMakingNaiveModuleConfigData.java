package InfrastructureManager.Modules.MatchMaking.Naive.RawData;

import InfrastructureManager.ModuleManagement.ModuleFactory.ModuleType;
import InfrastructureManager.ModuleManagement.RawData.ModuleConfigData;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MatchMakingNaiveModuleConfigData extends ModuleConfigData {

    public MatchMakingNaiveModuleConfigData(@JsonProperty("name") String name) {
        super(name);
        this.type = ModuleType.MATCH_MAKING_NAIVE;
    }
}
