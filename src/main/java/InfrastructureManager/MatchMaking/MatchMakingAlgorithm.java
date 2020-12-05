package InfrastructureManager.MatchMaking;

import InfrastructureManager.EdgeClient;
import InfrastructureManager.EdgeNode;

import java.util.HashMap;
import java.util.List;

/**
 * Interface for match making with different algortihm
 */
public interface MatchMakingAlgorithm {
    EdgeNode match (EdgeClient client, List<EdgeNode> nodeList, HashMap<String,HashMap<String,Long>> nodeHistory );
}
