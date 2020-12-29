package InfrastructureManager;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.util.*;

/**
 * This class is dedicated to fetch history related info.
 * From get history score, to history time to calculate the score.
 * Important function :
 * Also set historyMap and set ConnectedMap
 *
 * @author Zero
 */
public class EdgeClientHistory {
    private String id;
    //multimap with nodeID - history score
    private final HashMap<String, Long> nodeHistoryScoreHash = new HashMap<>();
    //multimap with client id - nodeID,connected time
    private final HashMap<String, Long> nodeLastConnectedTimeHash = new HashMap<>();

    public EdgeClientHistory(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }


    /**
     * Assign/update history score of node into client's historySCor
     * If node and its history score is already in here, replace that old value with nodeHistoryScore
     * If node is not registered in the hashMap, add the node and history score to the map
     *
     * @param nodeID           node's ID in String
     * @param nodeHistoryScore score that we want to update
     */
    public void setHistoryScore(String nodeID, long nodeHistoryScore) {
        this.nodeHistoryScoreHash.put(nodeID, nodeHistoryScore);
    }

    /**
     * Assign/update last connected time of node with client to
     *
     * @param nodeID                node's ID in String
     * @param nodeLastConnectedTime time node last connected to client
     */
    public void setLastConnectedTime(String nodeID, long nodeLastConnectedTime) {
        this.nodeLastConnectedTimeHash.put(nodeID, nodeLastConnectedTime);
    }


    public Long getConnectedTime(String nodeID) throws Exception {
        //TODO: custom exception for this
        //if node is not in the map
        if (!this.nodeLastConnectedTimeHash.containsKey(nodeID)){
            throw new Exception("can't find [" + nodeID + "] in the last connected map.");
        }

        return nodeLastConnectedTimeHash.get(nodeID);
    }

    /**
     * Get history score from client and node ID.
     *
     * @param nodeID node's ID in string
     * @return history score of node to client
     */
    public Long getHistoryScore(String nodeID) throws Exception {
        //TODO: custom exception for this
        //If node is not in the map
        if (!nodeHistoryScoreHash.containsKey(nodeID)) {
            throw new Exception("Can't find [" + nodeID + "] history's score in the map");
        }
        return nodeHistoryScoreHash.get(nodeID);
    }




    @Override
    public String toString() {
        return "Client         - [" + id + "]\n"
                + "score          - " + nodeHistoryScoreHash + "\n"
                + "connected time - " + nodeLastConnectedTimeHash;
    }

}

