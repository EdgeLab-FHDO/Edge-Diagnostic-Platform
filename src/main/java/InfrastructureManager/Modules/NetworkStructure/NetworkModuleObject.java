package InfrastructureManager.Modules.NetworkStructure;

import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;
import InfrastructureManager.PlatformObject;
import InfrastructureManager.Modules.NetworkStructure.Shared.SharedDistance;
import InfrastructureManager.Modules.NetworkStructure.Shared.SharedLocation;
public class NetworkModuleObject extends PlatformObject {
	public NetworkModuleObject(ImmutablePlatformModule ownerModule) {
		super(ownerModule);
	}

	public NetworkModuleObject(ImmutablePlatformModule ownerModule, String name) {
		super(ownerModule, name);
	}


	/*
    TODO: This is an example of a shared Location, change the type to whatever it is you are sharing,
     and add more methods like this if sharing more than one thing. Note that it depends on the interface (GlobalVarAccessNetworkModule)
	 */
	public SharedLocation getSharedLocation() {
		GlobalVarAccessNetworkModule casted = (GlobalVarAccessNetworkModule) this.getOwnerModule();
		return casted.getSharedLocation();
	}

	/*
    TODO: This is an example of a shared Location, change the type to whatever it is you are sharing,
     and add more methods like this if sharing more than one thing. Note that it depends on the interface (GlobalVarAccessNetworkModule)
	 */
	public SharedDistance getSharedDistance() {
		GlobalVarAccessNetworkModule casted = (GlobalVarAccessNetworkModule) this.getOwnerModule();
		return casted.getSharedDistance();
	}

}
