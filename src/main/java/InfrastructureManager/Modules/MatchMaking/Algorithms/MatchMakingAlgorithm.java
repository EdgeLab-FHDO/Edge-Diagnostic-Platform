package InfrastructureManager.Modules.MatchMaking.Algorithms;

import InfrastructureManager.Modules.MatchMaking.Client.EdgeClient;
import InfrastructureManager.Modules.MatchMaking.Node.EdgeNode;
import InfrastructureManager.Modules.MatchMaking.Exception.NoNodeFoundInHistoryException;
import InfrastructureManager.Modules.MatchMaking.Exception.NoNodeSatisfyRequirementException;

import java.util.List;

/**
 * Interface for match making with different algortihm
 */

//TODO: better abstraction/definition for match making algorithm (for better inheritance)
public interface MatchMakingAlgorithm {
    EdgeNode match (EdgeClient client, List<EdgeNode> nodeList) throws NoNodeSatisfyRequirementException, NoNodeFoundInHistoryException;
}
