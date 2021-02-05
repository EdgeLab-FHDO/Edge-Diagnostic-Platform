package InfrastructureManager.Modules.MatchMaking.ScoreBased;

import InfrastructureManager.ModuleManagement.RawData.ModuleConfigData;
import InfrastructureManager.Modules.MatchMaking.Input.MatchMakerInput;
import InfrastructureManager.Modules.MatchMaking.MatchMakingModule;
import InfrastructureManager.Modules.MatchMaking.MatchesList;
import InfrastructureManager.Modules.MatchMaking.Output.MatchMakerOutput;
import InfrastructureManager.Modules.MatchMaking.ScoreBased.RawData.MatchMakingScoreBasedModuleConfigData;

public class MatchMakingScoreBasedModule  extends MatchMakingModule {

    public MatchMakingScoreBasedModule() {
        super();
    }

    @Override
    public void configure(ModuleConfigData data) {
        super.configure(data);
        setAlgorithm(new ScoreBasedMatchMaking(this));
        String name = this.getName();
        MatchesList sharedList = this.getSharedList();
        MatchMakingScoreBasedModuleConfigData castedData = (MatchMakingScoreBasedModuleConfigData) data;
        setInputs(new MatchMakerInput(this, name, sharedList));
        setOutputs(new MatchMakerOutput(this, name, this.getAlgorithm(), sharedList));
    }
}
