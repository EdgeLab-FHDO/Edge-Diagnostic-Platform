package InfrastructureManager.Modules.NetworkStructure;
import InfrastructureManager.ModuleManagement.PlatformModule;
import InfrastructureManager.ModuleManagement.RawData.ModuleConfigData;
import InfrastructureManager.Modules.NetworkStructure.RawData.NetworkModuleConfigData;
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
		Network network = new Network();
		setInputs(new NetworkInput(this, name + ".network.in",network),
				new LocationInput(this, name + ".location.in",network),
				new DistanceInput(this,name + ".distance.in",network));
		setOutputs(new NetworkOutput(this,name + ".network.out",network),
				new LocationOutput(this,name + ".location.out",network),
				new DistanceOutput(this,name + ".distance.out",network));
	} 
	@Override
	public SharedLocation getSharedLocation(){
		return this.sharedLocation;
	}

	@Override
	public SharedDistance getSharedDistance(){
		return this.sharedDistance;
	}
}
