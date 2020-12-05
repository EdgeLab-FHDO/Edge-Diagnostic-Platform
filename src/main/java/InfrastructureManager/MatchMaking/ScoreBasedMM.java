package InfrastructureManager.MatchMaking;

import InfrastructureManager.EdgeClient;
import InfrastructureManager.EdgeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Score-based match making algorithm.
 * Score = Wd_D + Wc_C + Wn_N + Wh_H --- D = ping, C = res, N = network, H = history
 * Weight: 1 - 10 - 10 - 10
 *
 * @author: Zero
 */
public class ScoreBasedMM implements MatchMakingAlgorithm {


    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final long acceptablePing = 300; //use this as parameter before put them in the calculation
    long resource_Weight = 10;
    long network_Weight = 10;
    long ping_weight = 1;
    long history_weight = 10;
    HashMap<String, HashMap<String, Long>> nodeHistory;

    /* big step
    TODO: Score = Wd_D + Wc_C + Wn_N + Wh_H --- D = ping, C = res, N = network, H = history
        Weight: 1 - 10 - 10 - 10
    DONE: implement slots (resource and network), this should be an attribute in edge client and edge note.
    TODO: History implementation, using SQL? Or keep throwing JSON object back and forward?
    DONE: Need to make sure there won't be no duplicate in nodeList (Id and Address)
    TODO: Weight should be dynamic later on

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
    public EdgeNode match(EdgeClient thisClient, List<EdgeNode> nodeListInput, HashMap<String, HashMap<String, Long>> thisNodeHistory) {
        logger.info("match making started - Score based\n");

        //Initiating variable
        List<EdgeNode> nodeList = new ArrayList<EdgeNode>(nodeListInput); //In case of multi threading
        List<EdgeNode> acceptableNodesList = new ArrayList<>();
        EdgeNode bestNode = new EdgeNode();
        EdgeNode rejectNode = new EdgeNode("rejectNode", "000.000.000.000", false, Long.MAX_VALUE, Long.MAX_VALUE, Long.MAX_VALUE); //return this if score not good
        long bestScore = 0;
        long qosThreeshold = 100000; // TODO: this should be dynamic
        //May cause problem when multi thread.
        this.nodeHistory = thisNodeHistory;

        //initiate temp/comparing variable

        int numberOfUnqualified = 0; //to count the number of node we have been ruled out during iteration
        int totalNumberOfNode = nodeList.size();

        //Get client requirement (required resource, network)
        long require_resource = thisClient.getReqResource();
        long require_network = thisClient.getReqNetwork();

        //Get node's stats (ping, res, network, and history) and eliminate bad one
        for (EdgeNode thisNode : nodeList) {
            //List of comparing variables
            long nodeResource = thisNode.getResource();
            long nodeNetwork = thisNode.getNetwork();
            boolean nodeIsConnected = thisNode.isConnected();
            long pingNumber = getPing(thisClient, thisNode);
            long nodeHistoryWithClient = thisNodeHistory.get(thisNode.getId()).get(thisClient.getId());

            //Eliminate the one that's not good (small or equal are ruled out, equal is ruled out because it's better to have some sort of buffer rather than 100% utilization
            if ((nodeResource <= require_resource) || (nodeNetwork <= require_network) || !nodeIsConnected) {
                numberOfUnqualified++;
                //Skip this node if it doesn't have enough resources, network or not connected
                logger.info("node [{}] is skipped due to lacking of some criterion: failed case: {}/{} \n resources ( {} / {} ) --- network ( {} / {} ) --- connected : {} ",
                        numberOfUnqualified, totalNumberOfNode, thisNode.getId(), nodeResource, require_resource, nodeNetwork, require_network, nodeIsConnected);
            } else {
                //Find the one with the most available resources
                //DONE: change the criteria that we compare to resource + network + ping + history. Not just available resource like this

                //calculate current node score and find best node
                logger.info("calculating node [{}] score: ", thisNode.getId());
                long thisNodeScore = getScore(nodeResource, nodeNetwork, pingNumber, nodeHistoryWithClient);
                if (bestScore <= thisNodeScore) {
                    bestNode = thisNode;
                }

                //Adding good nodes into a list? Should be redundant? Or hashmap? Or none at all?
                acceptableNodesList.add(thisNode);
            }

        }

        //TODO: implement threshold, quality of service
        //Check whether it's good enough to match, or we just return the rejectedNode
        if (numberOfUnqualified >= totalNumberOfNode) {
            logger.warn(">>>>>>>FAIL<<<<<<< \nAll nodes are not satisfied the client resources requirement.");
            return rejectNode;
        }
        if (bestScore < qosThreeshold) {
            return bestNode;
        } else {
            logger.warn(">>>>>>>FAIL<<<<<<< \nThere is no node satisfy minimum quality of service, returning rejecting node.");
            return rejectNode;
        }
    }


    /**
     * Score = Wd_D + Wc_C + Wn_N + Wh_H --- D = ping, C = res, N = network, H = history
     * Weight: 1 - 10 - 10 - 10
     * Calculate node score after get rid of the bad nodes. Used this when we do have to iterate all nodes in the list for match making.
     *
     * @param nodeResource
     * @param nodeNetwork
     * @param pingNumber
     * @return calculatedScore
     * @author Zero
     */
    private long getScore(long nodeResource, long nodeNetwork, long pingNumber, long nodeHistoryWithClient) {
        //Initiate calculating variable
        long result = Long.MAX_VALUE;

        /* Calculate node score
        The more resource, more network; the better
        The less ping, less history; the better
         */
        long nodeScore = nodeResource * resource_Weight + nodeNetwork * network_Weight - pingNumber * ping_weight - nodeHistoryWithClient * history_weight;
        logger.info("node score {}   =   {} * {}   +    {} * {}   -    {} * {}    -   {} * {}",
                nodeScore, nodeResource, resource_Weight, nodeNetwork, network_Weight, pingNumber, ping_weight, nodeHistoryWithClient, history_weight);
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
     * @param thisClient
     * @param thisNode
     * @return pingResult - (network) distance between node and client
     * @author Zero
     */
    public long getPing(EdgeClient thisClient, EdgeNode thisNode) {
        long pingResult = 0;
        Random random = new Random();

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

        //checking for irregularity before sending out
        if (pingResult == 0) {
            logger.error("ping = 0 ms? Something fishy here, returning super big ping number \n");
            return Long.MAX_VALUE;
        } else return pingResult;
    }
}
