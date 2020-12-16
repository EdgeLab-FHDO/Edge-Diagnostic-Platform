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
    //multimap with client id - nodeID,history score
    private final Multimap<String, HashMap<String, Long>> historyMap = ArrayListMultimap.create();
    //multimap with client id - nodeID,connected time
    private final Multimap<String, HashMap<String, Long>> connectedMap = ArrayListMultimap.create();


    public EdgeClientHistory(String id) {
        this.id = id;
    }

    //put client ID in here, put client's connected node and node's score.
    //need to get time where client's connected to node
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    /**
     * Assign client, node and history score into class's multimap.
     * This function create a new HashMap, use mainly for initialize out collection.
     * Don't use to replace value, because we are using multimap, 1 key can have several similar value
     *
     * @param clientID    client's string ID
     * @param nodeID      node ID
     * @param nodeHistory history score between client and nodes
     */
    private void setHistoryMap(String clientID, String nodeID, Long nodeHistory) {
        HashMap<String, Long> nodeAndScore = new HashMap<>();
        nodeAndScore.put(nodeID, nodeHistory);
        this.historyMap.put(clientID, nodeAndScore);
    }


    /**
     * Assign update history score into this client's history with the node
     * If node and its history score is already in here, replace that old value with history score
     * If node is not registered/initialize, add the node and history score to the map
     *
     * @param clientID         clientID in String
     * @param nodeID           node's ID in String
     * @param nodeHistoryScore score that we want to update
     */
    public void setHistoryScoreForClient(String clientID, String nodeID, Long nodeHistoryScore) {
        Collection<HashMap<String, Long>> clientHistoryMapList = this.historyMap.get(clientID);
        //toggle to see whether nodeID is inside this client's historyMap or not
        boolean isNodeInTheMap = false;
        //TODO: check a better way for iterating the list.
        // idea: maybe use guava Tablemap, or maybe HashBasedTable (but this is basically Map<C,Map<K,V>>...will test later)
        for (HashMap<String, Long> theList : clientHistoryMapList) {
            //If the entry does have our nodeID, replace that node with new history score
            if (theList.containsKey(nodeID)) {
                //replace with new score
                theList.put(nodeID, nodeHistoryScore);
                isNodeInTheMap = true;
                break;
            }
        }
        //If the node is not registered in the system.
        if (!isNodeInTheMap) {
            // just add that node into the map with new history score
            HashMap<String, Long> nodeAndScore = new HashMap<>();
            nodeAndScore.put(nodeID, nodeHistoryScore);
            this.historyMap.put(clientID, nodeAndScore);
        }

    }

    /**
     * Assign client, node and connected time into class's multimap.
     * This function create a new HashMap, use mainly for initialize out collection.
     * Don't use to replace value, because we are using multimap, 1 key can have several similar value
     *
     * @param clientID      client's ID in String
     * @param nodeID        node's ID in string
     * @param connectedTime connect time between client and node
     */
    private void setConnectedMap(String clientID, String nodeID, Long connectedTime) {
        HashMap<String, Long> nodeAndTime = new HashMap<>();
        nodeAndTime.put(nodeID, connectedTime);
        this.connectedMap.put(clientID, nodeAndTime);
    }

    /**
     * Assign client, node and connected time into class's multimap.
     * If node and its connected time is already in here, replace that old value with connectedTime
     * If node is not registered/initialize, add the node and connectedTime to the map
     *
     * @param clientID      client's ID in String
     * @param nodeID        node's ID in string
     * @param connectedTime connect time between client and node
     */
    public void setConnectedTimeForClient(String clientID, String nodeID, Long connectedTime) {
        Collection<HashMap<String, Long>> clientConnectedMapList = this.connectedMap.get(clientID);
        //toggle to see whether nodeID is inside this client's connectedMap or not
        boolean isNodeInTheMap = false;

        //Check all entry of this client
        for (HashMap<String, Long> theList : clientConnectedMapList) {
            //If the entry does have our nodeID, replace that node with new connected time
            if (theList.containsKey(nodeID)) {
                //replace with new connected time
                theList.put(nodeID, connectedTime);
                isNodeInTheMap = true;
                break;
            }
        }

        //If the node is not registered in the system.
        if (!isNodeInTheMap) {
            // just add that node into the map with new connected time.
            HashMap<String, Long> nodeAndTime = new HashMap<>();
            nodeAndTime.put(nodeID, connectedTime);
            this.connectedMap.put(clientID, nodeAndTime);
        }
    }

    /**
     * Get time client's last connected to node
     *
     * @param clientID client's ID in String
     * @param nodeID   node's ID in string
     * @return the time client last connected to node in long
     */

    public Long getConnectedTime(String clientID, String nodeID) throws Exception {
        Long connectedTime = Long.MAX_VALUE;

        //get nodeAndTime list
        Collection<HashMap<String, Long>> nodeAndTimeList = this.connectedMap.get(clientID);
        boolean isNodeInTheMap = false;
        //get the correct nodeAndTime coupling
        for (HashMap<String, Long> nodeAndTime : nodeAndTimeList) {
            if (nodeAndTime.containsKey(nodeID)) {
                //Get connected time
                connectedTime = nodeAndTime.get(nodeID);
                isNodeInTheMap = true;
                break;
            }
        }

        if (!isNodeInTheMap) {
            throw new Exception("can't find [" + nodeID + "] in the map.");
        }

        return connectedTime;
    }

    /**
     * Get history score from client and node ID.
     *
     * @param clientID client's ID in string
     * @param nodeID   node's ID in string
     * @return history score of node to client
     */
    public Long getHistoryScore(String clientID, String nodeID) throws Exception {
        Long historyScore = Long.MAX_VALUE;
        //get nodeAndSCore list
        Collection<HashMap<String, Long>> nodeAndScoreList = this.historyMap.get(clientID);
        boolean isNodeInTheMap = false;

        //get the correct nodeAndScore coupling
        for (HashMap<String, Long> nodeAndScore : nodeAndScoreList) {
            if (nodeAndScore.containsKey(nodeID)) {
                //get history score
                historyScore = nodeAndScore.get(nodeID);
                isNodeInTheMap = true;
                break;
            }
        }

        if (!isNodeInTheMap) {
            throw new Exception("Can't find [" + nodeID + "] history's score in the map");
        }

        return historyScore;
    }


    @Override
    public String toString() {
        return "Client         - [" + id + "]\n"
                + "score          - " + historyMap + "\n"
                + "connected time - " + connectedMap;
    }

}
