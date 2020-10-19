package InfrastructureManager.MatchMaking;

import InfrastructureManager.EdgeClient;
import InfrastructureManager.EdgeNode;

public interface MatchMakingAlgorithm {
    EdgeNode match (EdgeClient client);
}
