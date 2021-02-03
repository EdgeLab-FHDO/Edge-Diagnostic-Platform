package InfrastructureManager.Modules.MatchMaking;

import InfrastructureManager.ModuleManagement.PlatformModule;
import InfrastructureManager.ModuleManagement.RawData.ModuleConfigData;

public class MatchMakingModule extends PlatformModule {

    private final MatchesList sharedList;
    private MatchMakingAlgorithm algorithm;

    public MatchMakingModule() {
        super();
        this.sharedList = new MatchesList();
    }

    protected MatchesList getSharedList() {
        return sharedList;
    }

    protected MatchMakingAlgorithm getAlgorithm() {
        return algorithm;
    }

    protected void setAlgorithm(MatchMakingAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    @Override
    public void configure(ModuleConfigData data) {
        String name = data.getName();
        setName(name);
    }
}
