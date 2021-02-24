package InfrastructureManager.Modules.NetworkStructure.Shared;


import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;
import InfrastructureManager.Modules.NetworkStructure.Location;
import InfrastructureManager.Modules.NetworkStructure.Network;
import InfrastructureManager.Modules.NetworkStructure.NetworkModuleObject;



public class SharedNetwork extends NetworkModuleObject {
	private Network network = new Network();
	
	public SharedNetwork(ImmutablePlatformModule ownerModule) {
		super(ownerModule);
	}
	
	public Location computeCurrentDeviceLocation(String deviceId)
	{
		return network.getCurrentDeviceLocation(network.getDevice(deviceId));
	}
	
	public Location computeCurrentApplicationLocation(String applicationId)
	{
		return network.getCurrentApplicationLocation(network.getApplicationInstance(applicationId));
	}
	
	public double computephysicalDistanceBetweenDevices(String sourceDeviceId, String destinationDeviceId) {
		return network.getphysicalDistanceBetweenDevices(sourceDeviceId,destinationDeviceId);
	}
	
	public double computephysicalDistanceBetweenApplications(String sourceDeviceId, String destinationDeviceId) {
		return network.getphysicalDistanceBetweenApplications(sourceDeviceId,destinationDeviceId);
	}
	
	public double computephysicalDistanceBetweenlocations(String sourceDeviceId, String destinationDeviceId) {
		return network.getphysicalDistanceBetweenlocations(sourceDeviceId,destinationDeviceId);
	}
	
	public void UpdateSharedNetworkObject(Network network) {
		this.network.copyNetwork(network);
	}
	
}
