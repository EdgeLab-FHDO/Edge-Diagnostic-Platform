package InfrastructureManager.MatchMaking;

import InfrastructureManager.EdgeClient;
import InfrastructureManager.EdgeNode;
import InfrastructureManager.MasterInput;
import InfrastructureManager.MasterOutput;

import java.util.HashMap;
import java.util.Map;

public class MatchMaker implements MasterInput, MasterOutput {

    private MatchMakingAlgorithm algorithm;
    private Map<EdgeClient, EdgeNode> matches;

    public MatchMaker() {
        this.matches = new HashMap<>();
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
