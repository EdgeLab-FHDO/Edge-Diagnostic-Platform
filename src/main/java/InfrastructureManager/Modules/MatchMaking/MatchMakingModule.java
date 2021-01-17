package InfrastructureManager.Modules.MatchMaking;

import InfrastructureManager.ModuleManagement.PlatformModule;
import InfrastructureManager.ModuleManagement.RawData.ModuleConfigData;
import InfrastructureManager.Modules.MatchMaking.Input.MatchMakerInput;
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
        setInputs(new MatchMakerInput(name + ".in", sharedList));
        setOutputs(new MatchMakerOutput(name + ".out", castedData.getMatchMakerType(), sharedList));
    }
}
