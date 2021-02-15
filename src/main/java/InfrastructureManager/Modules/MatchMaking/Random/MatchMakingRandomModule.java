package InfrastructureManager.Modules.MatchMaking.Random;

import InfrastructureManager.ModuleManagement.RawData.ModuleConfigData;
import InfrastructureManager.Modules.MatchMaking.Input.MatchMakerInput;
import InfrastructureManager.Modules.MatchMaking.MatchMakingModule;
import InfrastructureManager.Modules.MatchMaking.Output.MatchMakerOutput;
import InfrastructureManager.Modules.MatchMaking.Random.RawData.MatchMakingRandomModuleConfigData;

public class MatchMakingRandomModule extends MatchMakingModule {

    public MatchMakingRandomModule() {
        super();
    }

    @Override
    public void configure(ModuleConfigData data) {
        super.configure(data);
        setAlgorithm(new RandomMatchMaking(this));
        String name = this.getName();
        //If something from the data needs to be used to create IOS
        MatchMakingRandomModuleConfigData castedData = (MatchMakingRandomModuleConfigData) data;
        setInputs(new MatchMakerInput(this, name + ".in"));
        setOutputs(new MatchMakerOutput(this, name + ".out", this.getAlgorithm()));
    }
}
