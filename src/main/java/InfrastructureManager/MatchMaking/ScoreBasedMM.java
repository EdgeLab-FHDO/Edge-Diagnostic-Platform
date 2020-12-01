package InfrastructureManager.MatchMaking;

import InfrastructureManager.EdgeClient;
import InfrastructureManager.EdgeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ScoreBasedMM implements MatchMakingAlgorithm {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /* big step
    TODO: Score = Wd_D + Wc_C + Wn_N + Wh_H --- D = ping, C = res, N = network, H = history
    TODO: implement slots (resource and network), this should be an attribute in edge client and edge note.
    TODO: History implementation, using SQL? Or keep throwing JSON object back and forward?
    TODO: Need to make sure there won't be no duplicate in nodeList (Id and Address)
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
        EdgeNode rejectNode = new EdgeNode("rejectNode", "000.000.000.000", true, Long.MAX_VALUE); //return this if score not good
        long bestScore = 0;
        long qosThreeshold = 10; // TODO: this should be dynamic

        //Get client requirement (required resource, network)
        long require_resource = client.getReqResource();

        //Get node's stats (ping, res, network, and history) and eliminate bad one
        for (EdgeNode theNode : nodeList) {
            //List of comparing variables
            long nodeResource = theNode.getResource();

            //Eliminate the not good one
            if ((nodeResource < require_resource)) {
                //Skip this node if it doesn't have enough resources
                continue;
            } else {

                acceptableNodesList.add(theNode);
            }

        }

        //Get the score from all node in nodeList

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

        if (pingResult == 0) {
            logger.error("ping = 0 ms? Something fishy here, returning super big ping number \n");
            return Long.MAX_VALUE;
        } else return pingResult;
    }
}
