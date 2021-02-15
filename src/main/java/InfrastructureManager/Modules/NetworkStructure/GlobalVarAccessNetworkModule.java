package InfrastructureManager.Modules.NetworkStructure;

import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;

interface GlobalVarAccessNetworkModule extends ImmutablePlatformModule {
    String getSharedResource(); //TODO: This is an example of a String shared resource, change the type
}
