package InfrastructureManager.Modules.MatchMaking.Output;

import InfrastructureManager.MasterOutput;
import InfrastructureManager.Modules.MatchMaking.MatchMakerType;
import InfrastructureManager.Modules.MatchMaking.MatchesDoneList;

public class MatchMakerOutput extends MasterOutput {

    private final MatchMakerType type;
    private MatchesDoneList sharedMatchesList;

    public MatchMakerOutput(String name, MatchMakerType type, MatchesDoneList mapping) {
        super(name);
        this.type = type;
        this.sharedMatchesList = mapping;
    }

    @Override
    protected void out(String response) throws IllegalArgumentException {

    }
}
