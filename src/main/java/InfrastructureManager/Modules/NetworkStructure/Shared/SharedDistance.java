package InfrastructureManager.Modules.NetworkStructure.Shared;


import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;
import InfrastructureManager.Modules.NetworkStructure.NetworkModuleObject;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;


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
