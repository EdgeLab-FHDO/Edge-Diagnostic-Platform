package InfrastructureManager.Modules.MatchMaking.Random;

import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;
import InfrastructureManager.Modules.MatchMaking.MatchMakingAlgorithm;
import InfrastructureManager.Modules.MatchMaking.Client.EdgeClient;
import InfrastructureManager.Modules.MatchMaking.MatchMakingModuleObject;
import InfrastructureManager.Modules.MatchMaking.Node.EdgeNode;
import InfrastructureManager.PlatformObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Random;

public class RandomMatchMaking extends MatchMakingModuleObject implements MatchMakingAlgorithm {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public RandomMatchMaking(ImmutablePlatformModule ownerModule) {
        super(ownerModule);
    }

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
