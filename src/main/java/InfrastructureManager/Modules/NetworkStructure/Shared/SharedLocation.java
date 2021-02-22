package InfrastructureManager.Modules.NetworkStructure.Shared;


import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;
import InfrastructureManager.Modules.NetworkStructure.NetworkModuleObject;
import InfrastructureManager.Modules.NetworkStructure.Location;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class SharedLocation extends NetworkModuleObject {
	private final BlockingQueue locationQueue;
	private final ObjectMapper mapper;

	public SharedLocation(ImmutablePlatformModule ownerModule) {
		super(ownerModule);
		this.locationQueue = new ArrayBlockingQueue<>(25);
		this.mapper = new ObjectMapper();
	}


	public String getLocationAsJsonBody() throws JsonProcessingException, InterruptedException {
			return mapper.writeValueAsString(this.locationQueue.take());
	}

	public void putValuetoQueue(Location location) throws InterruptedException {
		this.locationQueue.put(location);
	}
}
