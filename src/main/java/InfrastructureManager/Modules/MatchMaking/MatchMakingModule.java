package InfrastructureManager.Modules.MatchMaking;

import InfrastructureManager.ModuleManagement.PlatformModule;
import InfrastructureManager.Modules.MatchMaking.Input.MatchMakerInput;
import InfrastructureManager.Modules.MatchMaking.Output.MatchMakerOutput;

public class MatchMakingModule extends PlatformModule {

    public MatchMakingModule(String name, MatchMakerType type) {
        super(name);
        MatchesList sharedList = new MatchesList();
        setInputs(new MatchMakerInput(name + ".in", sharedList));
        setOutputs(new MatchMakerOutput(name + ".out", type, sharedList));
    }
}
