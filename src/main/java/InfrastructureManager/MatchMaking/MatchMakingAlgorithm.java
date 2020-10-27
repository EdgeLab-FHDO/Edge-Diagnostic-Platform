package InfrastructureManager.MatchMaking;

import InfrastructureManager.EdgeClient;
import InfrastructureManager.EdgeNode;

import java.util.List;

public interface MatchMakingAlgorithm {
    EdgeNode match (EdgeClient client, List<EdgeNode> nodeList);
}
