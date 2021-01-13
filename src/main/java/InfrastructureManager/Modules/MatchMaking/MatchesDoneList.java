package InfrastructureManager.Modules.MatchMaking;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Semaphore;

public class MatchesDoneList {
    private final ConcurrentMap<String, String> matches; //Map Key:CLIENT_ID Value:NODE_JSON
    private final Semaphore block;
    private final ObjectMapper mapper;

    public MatchesDoneList() {
        this.matches = new ConcurrentHashMap<>();
        this.block = new Semaphore(0);
        this.mapper = new ObjectMapper();
    }

    public ConcurrentMap<String, String> getMapping() throws InterruptedException {
        this.block.acquire();
        return this.matches;
    }

    public void putValue(String key, String value) {
        this.matches.put(key, value);
        this.block.release();
    }
}
