package InfrastructureManager.MatchMaking;

import InfrastructureManager.EdgeClient;
import InfrastructureManager.EdgeNode;
import InfrastructureManager.EdgeClientHistory;

import java.util.List;

/**
 * Interface for match making with different algortihm
 */

//TODO: better abstraction/definition for match making algorithm (for better inheritance)
public interface MatchMakingAlgorithm {
    EdgeNode match (EdgeClient client, List<EdgeNode> nodeList, EdgeClientHistory clientHistory ) throws Exception;
}
