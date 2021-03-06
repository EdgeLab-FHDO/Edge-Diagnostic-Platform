package InfrastructureManager.Modules.MatchMaking.ScoreBased;

import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;
import InfrastructureManager.Modules.MatchMaking.MatchMakingAlgorithm;
import InfrastructureManager.Modules.MatchMaking.Client.EdgeClient;
import InfrastructureManager.Modules.MatchMaking.MatchMakingModuleObject;
import InfrastructureManager.Modules.MatchMaking.Node.EdgeNode;
import InfrastructureManager.Modules.MatchMaking.Client.EdgeClientHistory;
import InfrastructureManager.Modules.MatchMaking.Exception.NoNodeFoundInHistoryException;
import InfrastructureManager.Modules.MatchMaking.Exception.NoNodeSatisfyRequirementException;
import InfrastructureManager.PlatformObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Score-based match making algorithm.
 *
 * @author Zero
 */
public class ScoreBasedMatchMaking extends MatchMakingModuleObject implements MatchMakingAlgorithm {

    /*
    Score = Wd_D + Wc_C + Wn_N + Wh_H --- D = ping, C = res, N = network, H = history
    Weight: 1 - 10 - 10 - 10
     */

    //TODO: Weight should be dynamic
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final long ACCEPTABLE_PING = 300; //use this as parameter before put them in the calculation
    private static final long RESOURCE_WEIGHT = 10;
    private static final long NETWORK_WEIGHT = 10;
    private static final long PING_WEIGHT = 1;
    private static final long HISTORY_WEIGHT = 10;
    private static final long QOS_THRESHOLD = 100000;

    long deductScorePerSecond = 1;

    public ScoreBasedMatchMaking(ImmutablePlatformModule ownerModule) {
        super(ownerModule);
    }


    /**
     * score base match making function. Finding the best node to accomodate the client from a list of nodes.
     *
     * @param thisClient    client ID
     * @param nodeListInput list of nodes
     * @return best node to connect to client
     */
    @Override
    public EdgeNode match(EdgeClient thisClient, List<EdgeNode> nodeListInput) throws NoNodeSatisfyRequirementException, NoNodeFoundInHistoryException {
        logger.info("----------------------------MATCH MAKING - SCORE BASED--------------------\n" +
                        "R_weight, N_weight, D_weight, H_weight, QOS_threshold\n" +
                        "{}      , {}      , {}      , {}      , {}    "
                , RESOURCE_WEIGHT, NETWORK_WEIGHT, PING_WEIGHT, HISTORY_WEIGHT, QOS_THRESHOLD);


        //Initiating variable
        List<EdgeNode> nodeList = new ArrayList<>(nodeListInput); //In case of multi threading
        EdgeClientHistory clientHistory = thisClient.getClientHistory();

        //initiate temp/comparing variable
        int numberOfUnqualified = 0; //to count the number of node we have been ruled out during iteration
        int totalNumberOfNode = nodeList.size();
        long bestScore = 0;
        EdgeNode bestNode = new EdgeNode(this.getOwnerModule());

        //Get client requirement (required resource, network)
        long reqResource = thisClient.getReqResource();
        long reqNetwork = thisClient.getReqNetwork();

        //Get node's stats (ping, res, network, and history) and eliminate bad one
        for (EdgeNode thisNode : nodeList) {

            //TODO: Maybe having a class contain all the references to the info rather than create the variable in this class
            // doing so will make any change in node/client (cause by other class)  will be reflected in the variable in this class too
            //List of comparing variables
            String thisNodeID = thisNode.getId();
            long nodeResource = thisNode.getResource();
            long nodeNetwork = thisNode.getNetwork();
            boolean nodeIsConnected = thisNode.isConnected();
            long pingNumber = getPing(thisClient, thisNode);
            long nodeHistoryWithClient = clientHistory.getHistoryScore(thisNodeID);
            long lastConnectedTime = clientHistory.getConnectedTime(thisNodeID);
            boolean nodeIsOnline = thisNode.isOnline();

            //Get elapsed time to reduce history score
            long currentTime = System.currentTimeMillis();
            long elapsedTime = currentTime - lastConnectedTime;

            //Deducting score: -1 score for every second (1000ms)
            long deductingScore = deductScorePerSecond * elapsedTime / 1000;
            logger.info("time since last connected = {} ms, {} score deduct per second", elapsedTime, deductScorePerSecond);

            long nodeHistoryScore = nodeHistoryWithClient - deductingScore;

            if (nodeHistoryScore < 0) {
                nodeHistoryScore = 0;
            }
            logger.info("node history score = {} = {} - {}", nodeHistoryScore, nodeHistoryWithClient, deductingScore);
            logger.info("numberOfUnqualified = {}",numberOfUnqualified );
            //Update the score in the history info pack with this new score
            clientHistory.setHistoryScore(thisNodeID, nodeHistoryScore);
            clientHistory.setLastConnectedTime(thisNodeID, currentTime);

            //Eliminate the one that's not good (small or equal are ruled out, equal is ruled out because it's better to have some sort of buffer rather than 100% utilization
            if (nodeResource <= reqResource) {
                logger.info("[{}] doesn't have enough resource {}/{}", thisNodeID, nodeResource, reqResource);
                //next iteration if these requirement is not fulfill
                numberOfUnqualified++;
                continue;
            }
            if (nodeNetwork <= reqNetwork) {
                logger.info("[{}] doesn't have enough network bandwidth {}/{}", thisNodeID, nodeNetwork, reqNetwork);
                numberOfUnqualified++;
                continue;
            }
            if (!nodeIsConnected) {
                logger.info("[{}] is not connected", thisNodeID);
                numberOfUnqualified++;
                continue;
            }

            if(!nodeIsOnline){
                logger.info("[{}] is not online",thisNodeID);
                numberOfUnqualified++;
                continue;
            }

            if (pingNumber > ACCEPTABLE_PING) {
                logger.info("[{}] is too far away from client, ping number is not acceptable: {} > {}", thisNodeID, pingNumber, ACCEPTABLE_PING);
                numberOfUnqualified++;
                continue;
            }

            //calculate current node score and find best node
            logger.info("calculating [{}] score: ", thisNodeID);
            long thisNodeScore = 0;
            thisNodeScore = getScore(nodeResource, nodeNetwork, pingNumber, nodeHistoryScore);

            //compare it to the best score yet, if this node score is bigger then we will set it as new bestNode
            logger.info("best node's score - this node score:  {} - {} ", bestScore, thisNodeScore);
            if (bestScore < thisNodeScore) {
                bestNode = thisNode;
                bestScore = thisNodeScore;
            }
        }

        //When all nodes are unqualified.
        if (numberOfUnqualified >= totalNumberOfNode) {
            logger.warn("\n" +
                    ">>>>>>>>>>>>>>>>>>>>>>>>>    MATCH MAKING FAILED    <<<<<<<<<<<<<<<<<<<<<<<<\n" +
                    "No nodes in system satisfy required parameters (Network, Resource, Distance)\n");
            throw new NoNodeSatisfyRequirementException("\n" +
                    ">>>>>>>>>>>>>>>>>>>>>>>>>    MATCH MAKING FAILED    <<<<<<<<<<<<<<<<<<<<<<<<\n" +
                    "No nodes in system satisfy required parameters (Network, Resource, Distance)\n");
        }


        if (bestScore < QOS_THRESHOLD) {
            return bestNode;
        } else {
            logger.warn("\n" +
                    ">>>>>>>>>>>>>>>>>>>>>>>>>    MATCH MAKING FAILED    <<<<<<<<<<<<<<<<<<<<<<<<\n" +
                    "There is no node in list satisfy minimum quality of service\n");
            throw new NoNodeSatisfyRequirementException("\n" +
                    ">>>>>>>>>>>>>>>>>>>>>>>>>    MATCH MAKING FAILED    <<<<<<<<<<<<<<<<<<<<<<<<\n" +
                    "There is no node in list satisfy minimum quality of service\n");
        }

    }


    /**
     * Calculate node score after get rid of the bad nodes. Used this when we do have to iterate all nodes in the list for match making.
     *
     * @param nodeResource node's available resource
     * @param nodeNetwork  node's available network
     * @param pingNumber   distance between node and client
     * @return calculatedScore
     * @author Zero
     */
    private long getScore(long nodeResource, long nodeNetwork, long pingNumber, long nodeHistoryScore) {
        /*
        Score = Wd_D + Wc_C + Wn_N + Wh_H --- D = ping, C = res, N = network, H = history
        Weight: 1 - 10 - 10 - 10
         */

        //Initiate calculating variable
        long result;

        /* Calculate node score
        The more resource, more network; the better
        The less ping, less history; the better
         */
        long nodeScore = nodeResource * RESOURCE_WEIGHT + nodeNetwork * NETWORK_WEIGHT - pingNumber * PING_WEIGHT - nodeHistoryScore * HISTORY_WEIGHT;
        logger.info("node score {}   =   {} * {}   +    {} * {}   -    {} * {}    -   {} * {}",
                nodeScore, nodeResource, RESOURCE_WEIGHT, nodeNetwork, NETWORK_WEIGHT, pingNumber, PING_WEIGHT, nodeHistoryScore, HISTORY_WEIGHT);
        result = nodeScore;
        return result;
    }

    /**
     * Get Ping between client and node.
     * This should be dynamic base on some short of triangulation if possible. Or just throw random number for now
     * <p>
     * Ver 0.1: Using 2 int numbers, and calculate the distance between them.
     * E.g: client at 8, and node A at 10 and B 5, So client is closer to node A than B.
     * <p>
     * We can use this function for naive method implementation :))
     *
     * @param thisClient matching client
     * @param thisNode   matching node
     * @return pingResult - (network) distance between node and client
     * @author Zero
     */
    public long getPing(EdgeClient thisClient, EdgeNode thisNode) {
        long pingResult = -1;

        //Initiate variables for node and client, ready for calculation
        long thisClientLocation = thisClient.getLocation();
        long thisNodeLocation = thisNode.getLocation();
        long locationResult = 0;

        //only take positive value :)) abs would work too but I want this to be intuitive for whoever going to read it again
        if (thisClientLocation >= thisNodeLocation) {
            locationResult = thisClientLocation - thisNodeLocation;
        } else {
            locationResult = thisNodeLocation - thisClientLocation;
        }
        pingResult = locationResult;

        return pingResult;
    }
}
