package InfrastructureManager.Modules.MatchMaking.Algorithms;

import InfrastructureManager.EdgeClient;
import InfrastructureManager.EdgeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Random;

public class RandomMatchMaking implements MatchMakingAlgorithm {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public EdgeNode match(EdgeClient client, List<EdgeNode> nodeList) {
        logger.info("match making started - random \n");
        Random random = new Random();
        if (nodeList.isEmpty()) {
            logger.warn("there aren't any node in nodeList");

            return null;
        } else {
            for (EdgeNode thisNode : nodeList){
                logger.info("node - {}",thisNode.toString());
            }
            logger.info("random match making is done");
            return nodeList.get(random.nextInt(nodeList.size()));
        }

    }
}
