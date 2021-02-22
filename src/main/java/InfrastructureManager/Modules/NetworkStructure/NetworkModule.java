package InfrastructureManager.Modules.NetworkStructure;
import InfrastructureManager.ModuleManagement.PlatformModule;
import InfrastructureManager.ModuleManagement.RawData.ModuleConfigData;
import InfrastructureManager.Modules.NetworkStructure.RawData.NetworkModuleConfigData;
import InfrastructureManager.Modules.NetworkStructure.Shared.SharedDistance;
import InfrastructureManager.Modules.NetworkStructure.Shared.SharedLocation;
import InfrastructureManager.Modules.NetworkStructure.Input.DistanceInput;
import InfrastructureManager.Modules.NetworkStructure.Output.DistanceOutput;
import InfrastructureManager.Modules.NetworkStructure.Input.LocationInput;
import InfrastructureManager.Modules.NetworkStructure.Output.LocationOutput;
import InfrastructureManager.Modules.NetworkStructure.Input.NetworkInput;
import InfrastructureManager.Modules.NetworkStructure.Output.NetworkOutput;
/**
 * This class contains NetworkModule information.
 *
 * @author Shankar Lokeshwara
 */
public class NetworkModule extends PlatformModule implements GlobalVarAccessNetworkModule{
	private SharedLocation sharedLocation;
	private SharedDistance sharedDistance;
	private Network network = new Network();
	public NetworkModule() {
		super();
		this.sharedLocation = new SharedLocation(this);
		this.sharedDistance =  new SharedDistance(this);
	}
	@Override
	public void configure(ModuleConfigData data) {
		NetworkModuleConfigData castedData = (NetworkModuleConfigData) data;
		String name = data.getName();
		setName(name);

		setInputs(new NetworkInput(this, name + ".network.in",this.getNetwork()),
				new LocationInput(this, name + ".location.in"),
				new DistanceInput(this,name + ".distance.in"));
		setOutputs(new NetworkOutput(this,name + ".network.out",this.getNetwork()),
				new LocationOutput(this,name + ".location.out",this.getNetwork()),
				new DistanceOutput(this,name + ".distance.out",this.getNetwork()));
	} 
	@Override
	public SharedLocation getSharedLocation(){
		return this.sharedLocation;
	}

	@Override
	public SharedDistance getSharedDistance(){
		return this.sharedDistance;
	}
	
	protected Network getNetwork() {
        return network;
    }

    protected void setNetwork(Network network) {
        this.network = network;
    }

}
