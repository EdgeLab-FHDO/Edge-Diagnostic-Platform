package InfrastructureManager.Modules.MatchMaking.ScoreBased.RawData;

import InfrastructureManager.ModuleManagement.ModuleFactory.ModuleType;
import InfrastructureManager.ModuleManagement.RawData.ModuleConfigData;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MatchMakingScoreBasedModuleConfigData extends ModuleConfigData {

    public MatchMakingScoreBasedModuleConfigData(@JsonProperty("name") String name) {
        super(name);
        this.type = ModuleType.MATCH_MAKING_SCORE_BASED;
    }
}
