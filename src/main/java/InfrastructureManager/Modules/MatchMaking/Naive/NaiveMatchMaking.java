package InfrastructureManager.Modules.MatchMaking.Naive;

import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;
import InfrastructureManager.Modules.MatchMaking.MatchMakingAlgorithm;
import InfrastructureManager.Modules.MatchMaking.Client.EdgeClient;
import InfrastructureManager.Modules.MatchMaking.MatchMakingModuleObject;
import InfrastructureManager.Modules.MatchMaking.Node.EdgeNode;
import InfrastructureManager.Modules.MatchMaking.Exception.NoNodeSatisfyRequirementException;
import InfrastructureManager.PlatformObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class NaiveMatchMaking extends MatchMakingModuleObject implements MatchMakingAlgorithm {
    private static final long ACCEPTABLE_PING = 300; //use this as parameter before put them in the calculation
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public NaiveMatchMaking(ImmutablePlatformModule ownerModule) {
        super(ownerModule);
    }


    @Override
    public EdgeNode match(EdgeClient client, List<EdgeNode> nodeList) throws NoNodeSatisfyRequirementException {
        logger.info("----------------------------MATCH MAKING - NAIVE--------------------");
        long closestDistance = Long.MAX_VALUE;
        long clientLocation = client.getLocation();
        int totalNodeNumber = nodeList.size();
        EdgeNode bestNode = new EdgeNode(this.getOwnerModule());
        int numberOfUnqualified = 0;
        //Check distance between node and client
        for (EdgeNode thisNode : nodeList) {
            long distance = thisNode.getLocation() - clientLocation;
            //shortcut to take positive value only :))
            distance = Math.abs(distance);
            boolean nodeIsOnline = thisNode.isOnline();

            //Eliminate the one that's not good (small or equal are ruled out, equal is ruled out because it's better to have some sort of buffer rather than 100% utilization
            if (thisNode.getResource() <= client.getReqResource()) {
                logger.info("[{}] doesn't have enough resource {}/{}", thisNode.getId(), thisNode.getResource(), client.getReqResource());
                //next iteration if these requirement is not fulfill
                numberOfUnqualified++;
                continue;
            }
            if (thisNode.getNetwork() <= client.getReqNetwork()) {
                logger.info("[{}] doesn't have enough network bandwidth {}/{}", thisNode.getId(), thisNode.getNetwork(), client.getReqNetwork());
                numberOfUnqualified++;
                continue;
            }
            if (!thisNode.isConnected()) {
                logger.info("[{}] is not connected", thisNode.getId());
                numberOfUnqualified++;
                continue;
            }
            if (distance > ACCEPTABLE_PING) {
                logger.info("[{}] is too far away from client, ping number is not acceptable: {} > {}", thisNode.getId(), distance, ACCEPTABLE_PING);
                numberOfUnqualified++;
                continue;
            }

            if(!nodeIsOnline){
                logger.info("[{}] is not online",thisNode.getId());
                numberOfUnqualified++;
                continue;
            }

            if (numberOfUnqualified >= nodeList.size()) {
                logger.warn("\n" +
                        ">>>>>>>>>>>>>>>>>>>>>>>>>    MATCH MAKING FAILED    <<<<<<<<<<<<<<<<<<<<<<<<\n" +
                        "No nodes in system satisfy required parameters (Network, Resource, Distance)\n");
                throw new NoNodeSatisfyRequirementException("\n" +
                        ">>>>>>>>>>>>>>>>>>>>>>>>>    MATCH MAKING FAILED    <<<<<<<<<<<<<<<<<<<<<<<<\n" +
                        "No nodes in system satisfy required parameters (Network, Resource, Distance)\n");
            }

            logger.info("{} and {} distance = {} - {} = {} ", thisNode.getId(), client.getId(), thisNode.getLocation(), clientLocation, distance);
            //Find the minimum distance between client and all the node in list
            if (distance < closestDistance) {
                bestNode = thisNode;
                closestDistance = distance;
            }
        }
        logger.info("closest node to {} : {} - distance = {}", bestNode.getId(), client.getId(), closestDistance);
        return bestNode;
    }
}
