package InfrastructureManager.Modules.MatchMaking.RawData;

import InfrastructureManager.ModuleManagement.ModuleFactory.ModuleType;
import InfrastructureManager.ModuleManagement.RawData.ModuleConfigData;
import InfrastructureManager.Modules.MatchMaking.MatchMakerType;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MatchMakingModuleConfigData extends ModuleConfigData {

    private final MatchMakerType matchMakerType;

    public MatchMakingModuleConfigData(@JsonProperty("name") String name,
                                       @JsonProperty("matchMakerType") MatchMakerType matchMakerType) {
        super(name);
        this.type = ModuleType.MATCH_MAKING;
        this.matchMakerType = matchMakerType;
    }

    public MatchMakerType getMatchMakerType() {
        return matchMakerType;
    }
}
