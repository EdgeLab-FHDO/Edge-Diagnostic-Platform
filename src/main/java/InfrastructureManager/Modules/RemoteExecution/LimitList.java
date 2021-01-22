package InfrastructureManager.Modules.RemoteExecution;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Semaphore;

public class LimitList {
    private final ConcurrentMap<String, String> limitList;
    private final Semaphore block;
    private final ObjectMapper mapper;

    public LimitList() {
        this.limitList = new ConcurrentHashMap<>();
        this.block = new Semaphore(0);
        this.mapper = new ObjectMapper();
    }

    public ConcurrentMap<String, String> getLimitList() {
        return limitList;
    }

    public String getListAsBody() throws InterruptedException {
        this.block.acquire();
        try {
            return mapper.writeValueAsString(limitList);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    public void putValue(String key, String value) {
        this.limitList.put(key, value);
        this.block.release();
    }
}
