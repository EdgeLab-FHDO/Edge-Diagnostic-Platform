package InfrastructureManager;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * This class is dedicated to fetch history related info.
 * From get history score, to history time to calculate the score.
 * Implemented function : get historyScore - get ConnectedTime
 * Also set historyMap and set ConnectedMap
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
    public void setHistoryMap(String clientID, String nodeID, Long nodeHistory) {
        HashMap<String, Long> nodeAndScore = new HashMap<>();
        nodeAndScore.put(nodeID, nodeHistory);
        this.historyMap.put(clientID, nodeAndScore);
    }

    public void setHistoryScoreForClient(String clientID, String nodeID, Long nodeHistoryScore) {
        Collection<HashMap<String, Long>> clientHistoryMapList = this.historyMap.get(clientID);
        for (HashMap<String, Long> theList : clientHistoryMapList) {
            if (theList.containsKey(nodeID)) {
                //replace with new score
                theList.put(nodeID, nodeHistoryScore);
                break;
            }
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
    public void setConnectedMap(String clientID, String nodeID, Long connectedTime) {
        HashMap<String, Long> nodeAndTime = new HashMap<>();
        nodeAndTime.put(nodeID, connectedTime);
        this.connectedMap.put(clientID, nodeAndTime);
    }

    public void setConnectedTimeForClient(String clientID, String nodeID, Long connectedTime) {
        Collection<HashMap<String, Long>> clientConnectedMapList = this.connectedMap.get(clientID);
        for (HashMap<String, Long> theList : clientConnectedMapList) {
            if (theList.containsKey(nodeID)) {
                //replace with new connected time
                theList.put(nodeID, connectedTime);
                break;
            }
        }
    }

    /**
     * Get time client's last connected to node
     *
     * @param clientID client's ID in String
     * @param nodeID   node's ID in string
     * @return the time client last connected to node in long
     */

    public Long getConnectedTime(String clientID, String nodeID) {
        Long connectedTime = Long.MAX_VALUE;

        //get nodeAndTime list
        Collection<HashMap<String, Long>> nodeAndTimeList = this.connectedMap.get(clientID);

        //get the correct nodeAndTime coupling
        for (HashMap<String, Long> nodeAndTime : nodeAndTimeList) {
            if (nodeAndTime.containsKey(nodeID)) {
                //Get connected time
                connectedTime = nodeAndTime.get(nodeID);
                break;
            }
        }

        if (connectedTime >= Long.MAX_VALUE) {
            try {
                throw new Exception("can't find connected time in the map.");
            } catch (Exception e) {
                e.printStackTrace();
            }
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

    public Long getHistoryScore(String clientID, String nodeID) {
        Long historyScore = Long.MAX_VALUE;
        //get nodeAndSCore list
        Collection<HashMap<String, Long>> nodeAndScoreList = this.historyMap.get(clientID);

        //get the correct nodeAndScore coupling
        for (HashMap<String, Long> nodeAndScore : nodeAndScoreList) {
            if (nodeAndScore.containsKey(nodeID)) {
                //get history score
                historyScore = nodeAndScore.get(nodeID);
                break;
            }
        }

        if (historyScore >= Long.MAX_VALUE) {
            try {
                throw new Exception("Can't find the history's score in the map");
            } catch (Exception e) {
                e.printStackTrace();
            }
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

