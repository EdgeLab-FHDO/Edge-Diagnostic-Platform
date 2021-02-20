package InfrastructureManager.Modules.NetworkStructure;


import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Semaphore;

public class SharedDistance extends NetworkModuleObject {
	private final BlockingQueue distanceQueue;

	public SharedDistance(ImmutablePlatformModule ownerModule) {
		super(ownerModule);
		this.distanceQueue = new ArrayBlockingQueue<>(25);
	}


	public String getdistanceAsString() throws InterruptedException {
			return Double.toString((double) this.distanceQueue.take());
	}

	public void putValuetoQueue(double distance) throws InterruptedException {
		this.distanceQueue.put(distance);
	}
}
