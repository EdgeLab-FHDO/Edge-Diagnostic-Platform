package InfrastructureManager.Modules.RemoteExecution;

import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Semaphore;

/**
 * Represents a list that contains the created resource limits for different nodes.
 *
 * The values (limits) are characterized by a key-value format in which:
 * - key: Name of the node to which the limit will be applied (tag)
 * - value: CPU limit formatted like "QUOTA_PERIOD".
 *
 * A string representation of this list is sent as body when the limit script running in the different nodes
 * requests limits.
 *
 * Values are normally put by a {@link InfrastructureManager.Modules.RemoteExecution.Output.NodeLimitOutput} instance
 * after receiving the appropriate command to create them and are normally read by a {@link InfrastructureManager.Modules.RemoteExecution.Input.NodeLimitInput} in
 * order to sent them afterwards to a REST output.
 *
 * The list is multi-thread safe and blocks when waiting for a value to be put. It unblocks everytime a new value is put and then blocks again
 * to wait for a new value.
 *
 */
public class LimitList extends RemoteExecutionModuleObject {
    private final ConcurrentMap<String, String> limitList;
    private final Semaphore block;
    private final ObjectMapper mapper;

    /**
     * Constructor of the class. Creates a new Limit list.
     *
     * @param ownerModule the owner module of this list
     */
    public LimitList(ImmutablePlatformModule ownerModule) {
        super(ownerModule);
        this.limitList = new ConcurrentHashMap<>();
        this.block = new Semaphore(0);
        this.mapper = new ObjectMapper();
    }

    /**
     * Returns the list containing the limits in the format explained above
     *
     * @return List containing nodes and CPU limits
     */
    public ConcurrentMap<String, String> getList() {
        return limitList;
    }

    /**
     * Returns a JSON string representation of this list. This can be used for creating response bodies in
     * REST requests for example.
     *
     * This function blocks until a new value is put into the list using the {@link #putValue(String, String)} method
     *
     * @return JSON representation of the list. Returns null if an error parsing the list occurred
     * @throws InterruptedException If interrupted while blocked
     */
    public String getListAsBody() throws InterruptedException {
        this.block.acquire();
        try {
            return mapper.writeValueAsString(limitList);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    /**
     * Puts a new value into the list. Unblocks the list for reading
     *
     * @param key   Node tag
     * @param value CPU limit as "QUOTA_PERIOD"
     */
    public void putValue(String key, String value) {
        this.limitList.put(key, value);
        this.block.release();
    }
}
