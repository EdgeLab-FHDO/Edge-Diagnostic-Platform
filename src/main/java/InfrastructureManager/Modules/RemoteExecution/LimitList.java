package InfrastructureManager.Modules.RemoteExecution;

import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;
import InfrastructureManager.ModuleManagement.ModuleSharedResource;
import InfrastructureManager.PlatformObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Semaphore;

public class LimitList extends PlatformObject implements ModuleSharedResource {
    private final ConcurrentMap<String, String> limitList;
    private final Semaphore block;
    private final ObjectMapper mapper;

    public LimitList(ImmutablePlatformModule ownerModule) {
        super(ownerModule);
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

    private void putValue(String key, String value) {
        this.limitList.put(key, value);
        this.block.release();
    }

    @Override
    public void modify(String... data) {
        this.putValue(data[0],data[1]);
    }
}
