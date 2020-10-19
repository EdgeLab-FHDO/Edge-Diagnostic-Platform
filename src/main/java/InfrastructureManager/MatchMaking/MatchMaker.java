package InfrastructureManager.MatchMaking;

import InfrastructureManager.MasterInput;
import InfrastructureManager.MasterOutput;

public class MatchMaker implements MasterInput, MasterOutput {

    private MatchMakingAlgorithm algorithm;

    public MatchMaker() {

    }

    public void setAlgorithm(MatchMakingAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    @Override
    public String read() throws Exception {
        return null;
    }

    @Override
    public void out(String response) throws IllegalArgumentException {

    }
}
