package InfrastructureManager.MatchMaking;

import InfrastructureManager.EdgeClient;
import InfrastructureManager.EdgeNode;
import InfrastructureManager.Master;

import java.util.List;
import java.util.Random;

public class RandomMatchMaking implements MatchMakingAlgorithm {
    @Override
    public EdgeNode match(EdgeClient client) {
        Random random = new Random();
        List<EdgeNode> nodes = Master.getInstance().getAvailableNodes();
        return nodes.get(random.nextInt(nodes.size()));
    }
}
