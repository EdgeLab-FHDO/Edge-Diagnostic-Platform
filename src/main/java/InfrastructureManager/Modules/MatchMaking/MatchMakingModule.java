package InfrastructureManager.Modules.MatchMaking;

import InfrastructureManager.ModuleManagement.PlatformModule;
import InfrastructureManager.ModuleManagement.RawData.ModuleConfigData;
import InfrastructureManager.Modules.MatchMaking.Input.matchMakerInput;
import InfrastructureManager.Modules.MatchMaking.Output.MatchMakerOutput;
import InfrastructureManager.Modules.MatchMaking.RawData.MatchMakingModuleConfigData;

public class MatchMakingModule extends PlatformModule {

    public MatchMakingModule() {
        super();
    }

    @Override
    public void configure(ModuleConfigData data) {
        MatchMakingModuleConfigData castedData = (MatchMakingModuleConfigData) data;
        String name = castedData.getName();
        setName(name);
        MatchesList sharedList = new MatchesList();
        setInputs(new matchMakerInput(this, name + ".in", sharedList));
        setOutputs(new MatchMakerOutput(this,name + ".out", castedData.getMatchMakerType(), sharedList));
    }
}
