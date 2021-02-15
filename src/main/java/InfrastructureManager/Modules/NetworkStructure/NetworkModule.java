package InfrastructureManager.Modules.NetworkStructure;
import InfrastructureManager.ModuleManagement.PlatformModule;
import InfrastructureManager.ModuleManagement.RawData.ModuleConfigData;
import InfrastructureManager.Modules.NetworkStructure.RawData.NetworkModuleConfigData;
/**
 * This class contains NetworkModule information.
 *
 * @author Shankar Lokeshwara
 */
public class NetworkModule extends PlatformModule implements GlobalVarAccessNetworkModule{

    public NetworkModule() {
        super();
    }
    @Override
    public void configure(ModuleConfigData data) {
    	NetworkModuleConfigData castedData = (NetworkModuleConfigData) data;
    }

    @Override
    public String getSharedResource() { //TODO: This is an example with a String, change the type accordingly
        return null;
    }
}
