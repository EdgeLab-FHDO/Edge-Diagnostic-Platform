package InfrastructureManager.Modules.NetworkStructure;

import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;
import InfrastructureManager.PlatformObject;
import InfrastructureManager.Modules.NetworkStructure.Shared.SharedDistance;
import InfrastructureManager.Modules.NetworkStructure.Shared.SharedLocation;
import InfrastructureManager.Modules.NetworkStructure.Shared.SharedNetwork;
public class NetworkModuleObject extends PlatformObject {
	public NetworkModuleObject(ImmutablePlatformModule ownerModule) {
		super(ownerModule);
	}

	public NetworkModuleObject(ImmutablePlatformModule ownerModule, String name) {
		super(ownerModule, name);
	}


	/**
	 * Getter function to get the SharedLocation
	 * @return SharedLocation
	 */
	public SharedLocation getSharedLocation() {
		GlobalVarAccessNetworkModule casted = (GlobalVarAccessNetworkModule) this.getOwnerModule();
		return casted.getSharedLocation();
	}

	/**
	 * Getter function to get the SharedDistance
	 * @return SharedDistance
	 */
	public SharedDistance getSharedDistance() {
		GlobalVarAccessNetworkModule casted = (GlobalVarAccessNetworkModule) this.getOwnerModule();
		return casted.getSharedDistance();
	}

	/**
	 * Getter function to get the getSharedNetwork
	 * @return getSharedNetwork
	 */
	public SharedNetwork getSharedNetwork() {
		GlobalVarAccessNetworkModule casted = (GlobalVarAccessNetworkModule) this.getOwnerModule();
		return casted.getSharedNetwork();
	}
}
