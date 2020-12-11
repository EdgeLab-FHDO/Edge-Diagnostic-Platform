package InfrastructureManager.MatchMaking;

import InfrastructureManager.EdgeClient;
import InfrastructureManager.EdgeNode;
import InfrastructureManager.EdgeClientHistory;
import com.google.common.collect.Multimap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Score-based match making algorithm.
 * Score = Wd_D + Wc_C + Wn_N + Wh_H --- D = ping, C = res, N = network, H = history
 * Weight: 1 - 10 - 10 - 10
 *
 * @author Zero
 */
public class ScoreBasedMM implements MatchMakingAlgorithm {


    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    // TODO: this should be dynamic
    private static final long ACCEPTABLE_PING = 300; //use this as parameter before put them in the calculation
    private static final long RESOURCE_WEIGHT = 10;
    private static final long NETWORK_WEIGHT = 10;
    private static final long PING_WEIGHT = 1;
    private static final long HISTORY_WEIGHT = 10;
    private static final long QOS_THRESHOLD = 100000;

    Multimap<String, HashMap<String, Long>> nodeHistory;
    EdgeClientHistory clientHistory;
    long deductScorePerSecond = 1;

    /* big step
    DONE: Score = Wd_D + Wc_C + Wn_N + Wh_H --- D = ping, C = res, N = network, H = history
        Weight: 1 - 10 - 10 - 10
    DONE: implement slots (resource and network), this should be an attribute in edge client and edge note.
    TODO: History implementation, using SQL? Or keep throwing JSON object back and forward?
    DONE: Need to make sure there won't be no duplicate in nodeList (Id and Address)
    TODO: Weight should be dynamic later on
    TODO: Implement time

     */

    /*
    TODO: implement visibility: blind-inacc-accurate. This rather connected to "input" analyze,
     take the input and somehow "blur" the info by introduce some deviant (multiply by 5-30%, so we get the estimation)

     */

    /* small step
    DONE: match base on available resources
    DONE: eliminate nodes that can't accommodate client (not enough resources, network etc)
    DONE: compare between "acceptable" node using score
     */
    @Override
    public EdgeNode match(EdgeClient thisClient, List<EdgeNode> nodeListInput, EdgeClientHistory thisClientHistory) {
        logger.info("""
                
                ----------------------------MATCH MAKING - SCORE BASED--------------------
                R_weight, N_weight, D_weight, H_weight, QOS_threshold
                {}      , {}      , {}      , {}      , {}    """
                , RESOURCE_WEIGHT, NETWORK_WEIGHT, PING_WEIGHT, HISTORY_WEIGHT, QOS_THRESHOLD);

        //Initiating variable
        List<EdgeNode> nodeList = new ArrayList<>(nodeListInput); //In case of multi threading
        EdgeNode bestNode = new EdgeNode();
        EdgeNode rejectNode = new EdgeNode("rejectNode", "000.000.000.000", false, Long.MAX_VALUE, Long.MAX_VALUE, Long.MAX_VALUE, Long.MAX_VALUE, Long.MAX_VALUE); //return this if score not good
        long bestScore = 0;

        this.clientHistory = thisClientHistory;

        //initiate temp/comparing variable

        int numberOfUnqualified = 0; //to count the number of node we have been ruled out during iteration
        int totalNumberOfNode = nodeList.size();

        //Get client requirement (required resource, network)
        String thisClientID = thisClient.getId();
        long reqResource = thisClient.getReqResource();
        long reqNetwork = thisClient.getReqNetwork();

        //Get node's stats (ping, res, network, and history) and eliminate bad one
        for (EdgeNode thisNode : nodeList) {
            //List of comparing variables
            String thisNodeID = thisNode.getId();
            long nodeResource = thisNode.getResource();
            long nodeNetwork = thisNode.getNetwork();
            boolean nodeIsConnected = thisNode.isConnected();
            long pingNumber = getPing(thisClient, thisNode);
            long nodeHistoryWithClient = clientHistory.getHistoryScore(thisClientID, thisNodeID);
            long lastConnectedTime = clientHistory.getConnectedTime(thisClientID, thisNodeID);

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
            //Update the score in the history info pack with this new score
            clientHistory.setHistoryScoreForClient(thisClientID, thisNodeID, nodeHistoryScore);

            //Eliminate the one that's not good (small or equal are ruled out, equal is ruled out because it's better to have some sort of buffer rather than 100% utilization
            if ((nodeResource <= reqResource) || (nodeNetwork <= reqNetwork) || !nodeIsConnected || pingNumber > ACCEPTABLE_PING) {
                numberOfUnqualified++;
                //Skip this node if it doesn't have enough resources, network or not connected
                logger.info("[{}] is skipped due to lacking of some criterion: failed case: {}/{} \n resources ( {} / {} ) --- network ( {} / {} ) --- connected : {} --- ping ( {} / {} )",
                        thisNode.getId(), numberOfUnqualified, totalNumberOfNode, nodeResource, reqResource, nodeNetwork, reqNetwork, nodeIsConnected, pingNumber, ACCEPTABLE_PING);
            } else {
                //Find the one with the most available resources
                //DONE: change the criteria that we compare to resource + network + ping + history. Not just available resource like this

                //calculate current node score and find best node
                logger.info("calculating [{}] score: ", thisNodeID);
                long thisNodeScore = getScore(nodeResource, nodeNetwork, pingNumber, nodeHistoryScore);
                //TODO: change this to debug
                logger.debug("best node's score - this node score:  {} - {} ", bestScore, thisNodeScore);
                if (bestScore < thisNodeScore) {
                    bestNode = thisNode;
                    bestScore = thisNodeScore;
                }
            }

        }

        //TODO: implement threshold, quality of service
        //Check whether it's good enough to match, or we just return the rejectedNode
        if (numberOfUnqualified >= totalNumberOfNode) {
            logger.warn("""
                    >>>>>>>>>>>>>>>>>>>>>>>>>    MATCH MAKING FAILED    <<<<<<<<<<<<<<<<<<<<<<<<  
                    No nodes in system satisfy required parameters (Network, Resource, Distance)
                    """);
            return rejectNode;
        }
        if (bestScore < QOS_THRESHOLD) {
            return bestNode;
        } else {
            logger.warn("""
                    >>>>>>>>>>>>>>>>>>>>>>>>>    MATCH MAKING FAILED    <<<<<<<<<<<<<<<<<<<<<<<<  
                    There is no node satisfy minimum quality of service ({}), returning rejecting node.
                    """, QOS_THRESHOLD);
            return rejectNode;
        }
    }


    /**
     * Score = Wd_D + Wc_C + Wn_N + Wh_H --- D = ping, C = res, N = network, H = history
     * Weight: 1 - 10 - 10 - 10
     * Calculate node score after get rid of the bad nodes. Used this when we do have to iterate all nodes in the list for match making.
     *
     * @param nodeResource  node's available resource
     * @param nodeNetwork node's available network
     * @param pingNumber distance between node and client
     * @return calculatedScore
     * @author Zero
     */
    private long getScore(long nodeResource, long nodeNetwork, long pingNumber, long nodeHistoryScore) {
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
        // compare with Long.MAX_VALUE because when network, resource or ping is wrong in some step, it gonna return Long.MAX_VALUE. Making the equation value very big
        if (result >= Long.MAX_VALUE) {
            logger.warn("this nodeScore value is too huge, something is wrong with either input number or the weight");
        }
        return result;
    }

    /**
     * -----------------------Get Ping between client & node-----------------------
     * This should be dynamic base on some short of triangulation if possible. Or just throw random number for now
     * <p>
     * Ver 0.1: Using 2 int numbers, and calculate the distance between them.
     * E.g: client at 8, and node A at 10 and B 5, So client is closer to node A than B.
     * <p>
     * We can use this function for naive method implementation :))
     *
     * @param thisClient matching client
     * @param thisNode matching node
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

        //introduce noise or visibility here. Or just do it in the match function above, don't know.
        pingResult = locationResult;

        //checking for irregularity before sending out. This step is rather redundant since this would NEVER happen.
        if (pingResult < 0) {
            logger.error("""
                    -------------------------------GETTING PING FAILED----------------------------
                    [{}] location - {}, [{}] location - {} 
                    ping is negative? Something fishy here, returning super big ping number"""
                    , thisNode.getId(), thisNode.getLocation(), thisClient.getId(), thisClient.getLocation());
            return Long.MAX_VALUE;
        } else return pingResult;
    }
}
