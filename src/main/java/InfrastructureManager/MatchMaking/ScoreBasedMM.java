package InfrastructureManager.MatchMaking;

import InfrastructureManager.EdgeClient;
import InfrastructureManager.EdgeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ScoreBasedMM implements MatchMakingAlgorithm {


    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /* big step
    TODO: Score = Wd_D + Wc_C + Wn_N + Wh_H --- D = ping, C = res, N = network, H = history
        Weight: 1 - 10 - 10 - 10
    TODO: implement slots (resource and network), this should be an attribute in edge client and edge note.
    TODO: History implementation, using SQL? Or keep throwing JSON object back and forward?
    TODO: Need to make sure there won't be no duplicate in nodeList (Id and Address)
     */

    /*
    TODO: implement visibility: blind-inacc-accurate. This rather connected to "input" analyze,
     take the input and somehow "blur" the info by introduce some deviant (multiply by 5-30%, so we get the estimation)

     */

    /* small step
    TODO: match base on available resources
    TODO: eliminate nodes that can't accommodate client (not enough resources, network etc)
    TODO: compare between "acceptable" node using score
     */
    @Override
    public EdgeNode match(EdgeClient client, List<EdgeNode> nodeList) {
        logger.info("match making started - Score based\n");

        //Initiating variable
        List<EdgeNode> acceptableNodesList = new ArrayList<>();
        EdgeNode bestNode = new EdgeNode();
        EdgeNode rejectNode = new EdgeNode("rejectNode", "000.000.000.000", false, Long.MAX_VALUE, Long.MAX_VALUE, Long.MAX_VALUE); //return this if score not good
        long bestScore = 0;
        long qosThreeshold = 10; // TODO: this should be dynamic
        //initiate temp/comparing  variable
        long leftResource = 0;//resource available after connected

        //Get client requirement (required resource, network)
        long require_resource = client.getReqResource();
        long require_network = client.getReqNetwork();

        //Get node's stats (ping, res, network, and history) and eliminate bad one
        for (EdgeNode theNode : nodeList) {
            //List of comparing variables
            long nodeResource = theNode.getResource();
            long nodeNetwork = theNode.getNetwork();

            //Eliminate the one that's not good (small or equal are ruled out, equal is ruled out because it's better to have some sort of buffer rather than 100% ultilization
            if ((nodeResource <= require_resource || nodeNetwork <= require_network)) {

                //Skip this node if it doesn't have enough resources, net work
                logger.info("node skipped [{}] due to lacking of some criterion \n resources ( {} / {} ) --- network ( {} / {} ) ", theNode.getId(), nodeResource, require_resource, nodeNetwork, require_network);

            } else {
                //Find the one with the most available resources
                //TODO: change the criteria that we compare to resource + network + ping + history. Not just available resource like this
                long thisNodeLeftResource = nodeResource - require_resource;
                if (thisNodeLeftResource > leftResource) {
                    leftResource = thisNodeLeftResource;
                    bestNode = theNode;
                }
                //TODO: calculate current node score and find best node

                //Adding good nodes into a list? Should be redundance? Or hashmap? Or none at all?
                acceptableNodesList.add(theNode);
            }

        }

        //Check whether it's good enough to match, or we just return the rejectedNode
        if (bestScore < qosThreeshold) {
            return bestNode;
        } else return rejectNode;
    }

    /*
    -----------------------Get Ping between client & node-----------------------
    This should be dynamic base on some short of triangulation if possible. Or just throw random number for now
     */
    private long getPing(EdgeClient thisClient, EdgeNode thisNode) {
        long pingResult = 0;

        Random random = new Random();

        if (pingResult == 0) {
            logger.error("ping = 0 ms? Something fishy here, returning super big ping number \n");
            return Long.MAX_VALUE;
        } else return pingResult;
    }
}
