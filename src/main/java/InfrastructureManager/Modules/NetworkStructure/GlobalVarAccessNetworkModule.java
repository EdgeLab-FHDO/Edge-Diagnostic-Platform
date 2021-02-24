package InfrastructureManager.Modules.NetworkStructure;

import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;
import InfrastructureManager.Modules.NetworkStructure.Shared.SharedDistance;
import InfrastructureManager.Modules.NetworkStructure.Shared.SharedLocation;
import InfrastructureManager.Modules.NetworkStructure.Shared.SharedNetwork;

interface GlobalVarAccessNetworkModule extends ImmutablePlatformModule {
	SharedLocation getSharedLocation(); // Shared variable for location
	SharedDistance getSharedDistance(); //Shared variable for physical or network distances
	SharedNetwork getSharedNetwork(); // Shared network object

}
