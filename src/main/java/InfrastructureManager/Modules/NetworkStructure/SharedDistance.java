package InfrastructureManager.Modules.NetworkStructure;


import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Semaphore;

public class SharedDistance extends NetworkModuleObject {
	private final ConcurrentMap<String, Double> distanceList;
	private final Semaphore block;
	private final ObjectMapper mapper;

	public SharedDistance(ImmutablePlatformModule ownerModule) {
		super(ownerModule);
		this.distanceList = new ConcurrentHashMap<>();
		this.block = new Semaphore(0);
		this.mapper = new ObjectMapper();
	}

	public ConcurrentMap<String, Double> getDistanceList() {
		return distanceList;
	}

	public String getListAsBody() throws InterruptedException {
		this.block.acquire();
		try {
			return mapper.writeValueAsString(distanceList);
		} catch (JsonProcessingException e) {
			return null;
		}
	}

	public void putValue(String key, double distance) {
		this.distanceList.put(key, distance);
		this.block.release();
	}
}
