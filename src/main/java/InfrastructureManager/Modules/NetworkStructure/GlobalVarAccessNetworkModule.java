package InfrastructureManager.Modules.NetworkStructure;

import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;

interface GlobalVarAccessNetworkModule extends ImmutablePlatformModule {
	SharedLocation getSharedLocation(); // Shared variable for location
	SharedDistance getSharedDistance(); //Shared variable for physical or network distances
}
