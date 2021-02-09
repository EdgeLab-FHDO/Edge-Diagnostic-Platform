package InfrastructureManager.Modules.MatchMaking.Naive;

import InfrastructureManager.ModuleManagement.RawData.ModuleConfigData;
import InfrastructureManager.Modules.MatchMaking.Input.MatchMakerInput;
import InfrastructureManager.Modules.MatchMaking.MatchMakingModule;
import InfrastructureManager.Modules.MatchMaking.MatchesList;
import InfrastructureManager.Modules.MatchMaking.Naive.RawData.MatchMakingNaiveModuleConfigData;
import InfrastructureManager.Modules.MatchMaking.Output.MatchMakerOutput;

public class MatchMakingNaiveModule  extends MatchMakingModule {

    public MatchMakingNaiveModule() {
        super();
    }

    @Override
    public void configure(ModuleConfigData data) {
        super.configure(data);
        setAlgorithm(new NaiveMatchMaking(this));
        String name = this.getName();
        MatchesList sharedList = this.getSharedList();
        MatchMakingNaiveModuleConfigData castedData = (MatchMakingNaiveModuleConfigData) data;
        setInputs(new MatchMakerInput(this, name + ".in", sharedList));
        setOutputs(new MatchMakerOutput(this, name + ".out", this.getAlgorithm(), sharedList));
    }
}
