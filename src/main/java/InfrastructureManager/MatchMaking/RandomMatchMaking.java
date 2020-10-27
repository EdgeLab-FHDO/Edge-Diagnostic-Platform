package InfrastructureManager.MatchMaking;

import InfrastructureManager.EdgeClient;
import InfrastructureManager.EdgeNode;
import InfrastructureManager.Master;

import java.util.List;
import java.util.Random;

public class RandomMatchMaking implements MatchMakingAlgorithm {
    @Override
    public EdgeNode match(EdgeClient client, List<EdgeNode> nodeList) {
        Random random = new Random();
        if (nodeList.isEmpty()) {
            return null;
        } else {
            return nodeList.get(random.nextInt(nodeList.size()));
        }
    }
}
