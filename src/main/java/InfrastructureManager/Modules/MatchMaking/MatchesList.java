package InfrastructureManager.Modules.MatchMaking;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;

public class MatchesList {
    private final ConcurrentMap<String, String> matches; //Map Key:CLIENT_ID Value:NODE_JSON
    private final Semaphore block;
    private String lastClientAdded;

    public MatchesList() {
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

    public void putValue(String clientID, String nodeAsString) {
        this.matches.put(clientID, nodeAsString);
        this.lastClientAdded = clientID;
        this.block.release();
    }

    public boolean clientIsConnected(String clientID) {
        return matches.containsKey(clientID);
    }

    public boolean nodeIsAssigned(String nodeAsString) {
        return matches.containsValue(nodeAsString);
    }

    public List<String> getConnectedClientsToNode(String nodeAsString) {
        return matches.entrySet().stream()
                .filter(e -> e.getValue().equals(nodeAsString))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public String removeClient(String clientID) {
        return matches.remove(clientID);
    }
}
