package InfrastructureManager.Modules.NetworkStructure;
import InfrastructureManager.ModuleManagement.PlatformModule;
import InfrastructureManager.ModuleManagement.RawData.ModuleConfigData;
import InfrastructureManager.Modules.NetworkStructure.RawData.NetworkModuleConfigData;
import InfrastructureManager.Modules.NetworkStructure.Shared.SharedDistance;
import InfrastructureManager.Modules.NetworkStructure.Shared.SharedLocation;
import InfrastructureManager.Modules.NetworkStructure.Shared.SharedNetwork;
import InfrastructureManager.Modules.NetworkStructure.Input.DistanceInput;
import InfrastructureManager.Modules.NetworkStructure.Output.DistanceOutput;
import InfrastructureManager.Modules.NetworkStructure.Input.LocationInput;
import InfrastructureManager.Modules.NetworkStructure.Output.LocationOutput;
/**
 * This class contains NetworkModule information.
 *
 * @author Shankar Lokeshwara
 */
public class NetworkModule extends PlatformModule implements GlobalVarAccessNetworkModule{
	private SharedLocation sharedLocation;
	private SharedDistance sharedDistance;
	private SharedNetwork  sharedNetwork;
	public NetworkModule() {
		super();
		this.sharedLocation = new SharedLocation(this);
		this.sharedDistance =  new SharedDistance(this);
		this.sharedNetwork = new SharedNetwork(this);
	}
	@Override
	public void configure(ModuleConfigData data) {
		NetworkModuleConfigData castedData = (NetworkModuleConfigData) data;
		String name = data.getName();
		setName(name);

		setInputs(new LocationInput(this, name + ".location.in"),
				new DistanceInput(this,name + ".distance.in"));
		setOutputs(new LocationOutput(this,name + ".location.out"),
				new DistanceOutput(this,name + ".distance.out"));
	} 
	@Override
	public SharedLocation getSharedLocation(){
		return this.sharedLocation;
	}

	@Override
	public SharedDistance getSharedDistance(){
		return this.sharedDistance;
	}
	
	@Override
	public SharedNetwork getSharedNetwork() {
		return this.sharedNetwork;
	}

}
