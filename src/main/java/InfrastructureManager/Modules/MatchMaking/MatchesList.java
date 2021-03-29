package InfrastructureManager.Modules.MatchMaking;

import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;
import InfrastructureManager.Modules.MatchMaking.Exception.ClientNotAssignedException;
import InfrastructureManager.Modules.MatchMaking.Node.EdgeNode;
import InfrastructureManager.PlatformObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;

public class MatchesList extends MatchMakingModuleObject {
    private final ConcurrentMap<String, String> matches; //Map Key:CLIENT_ID Value:NODE_JSON
    private final Semaphore block;
    private String lastClientAdded;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public MatchesList(ImmutablePlatformModule ownerModule) {
        super(ownerModule);
        this.matches = new ConcurrentHashMap<>();
        this.block = new Semaphore(0);
    }

    public ConcurrentMap<String, String> getMapping() {
        return this.matches;
    }

    public String getLastAdded() throws InterruptedException {
        this.block.acquire();
        return lastClientAdded + " " + this.matches.get(lastClientAdded);
    }

    public void putValue(String clientID, String nodeJSONbody) {
        logger.info("clientID = {}\nnode = {}",clientID,nodeJSONbody);
        this.matches.put(clientID, nodeJSONbody);
        this.lastClientAdded = clientID;
        this.block.release();
    }

    public boolean clientIsConnected(String clientID) {
        return matches.containsKey(clientID);
    }

    public boolean nodeIsAssigned(String nodeID) {
        return matches.containsValue(nodeID);
    }


    /**
     * get the node name in the JSON body.
     * @param clientID
     * @return
     */
    public String getClientConnectedNodeID(String clientID) {
        //fetch the JSON body, look like this
        //{"id":"node1","ipAddress":"68.131.232.215:30968","connected":true,"resource":200,"network":200,"location":55,"totalResource":200,"totalNetwork":200,"heartBeatInterval":15000,"online":true}
        String nodeJSON = this.matches.get(clientID);
        //get the index of the comma
        String comma = ",";
        int commaIndex = nodeJSON.indexOf(comma);
        //now get that substring from position 7 to the first comma.
        String nodeID = nodeJSON.substring(7, commaIndex - 1);

        return nodeID;
    }


    /**
     * return a list of clients that connected to thisNode
     * @param nodeID thisNode's in String
     * @return
     */
    public List<String> getConnectedClientsToNode(String nodeID) {
        return matches.entrySet().stream()
                .filter(e -> e.getValue().equals(nodeID))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public String removeClient(String clientID) {
        return matches.remove(clientID);
    }
}
