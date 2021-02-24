package InfrastructureManager.Modules.NetworkStructure.Shared;


import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;
import InfrastructureManager.Modules.NetworkStructure.NetworkModuleObject;
import java.util.concurrent.LinkedBlockingQueue;


public class SharedDistance extends NetworkModuleObject {
	private final LinkedBlockingQueue<Double> distanceQueue;

	public SharedDistance(ImmutablePlatformModule ownerModule) {
		super(ownerModule);
		this.distanceQueue = new LinkedBlockingQueue<Double>();
	}
	
	public String getdistanceAsString() throws InterruptedException {
			return Double.toString((double) this.distanceQueue.take());
	}

	public void putValuetoQueue(double distance) throws InterruptedException {
		this.distanceQueue.put(distance);
	}
}
