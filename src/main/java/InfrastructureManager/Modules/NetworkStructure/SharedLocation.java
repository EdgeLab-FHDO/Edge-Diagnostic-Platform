package InfrastructureManager.Modules.NetworkStructure;


import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Semaphore;

public class SharedLocation extends NetworkModuleObject {
	private final ConcurrentMap<String, Location> locationList;
	private final Semaphore block;
	private final ObjectMapper mapper;

	public SharedLocation(ImmutablePlatformModule ownerModule) {
		super(ownerModule);
		this.locationList = new ConcurrentHashMap<>();
		this.block = new Semaphore(0);
		this.mapper = new ObjectMapper();
	}

	public ConcurrentMap<String, Location> getLocationList() {
		return locationList;
	}

	public String getListAsBody() throws InterruptedException {
		this.block.acquire();
		try {
			return mapper.writeValueAsString(locationList);
		} catch (JsonProcessingException e) {
			return null;
		}
	}

	public void putValue(String key, Location location) {
		this.locationList.put(key, location);
		this.block.release();
	}
}
