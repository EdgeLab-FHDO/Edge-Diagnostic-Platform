package InfrastructureManager.Modules.NetworkStructure.Output;

import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;
import InfrastructureManager.ModuleManagement.PlatformOutput;
import InfrastructureManager.Modules.NetworkStructure.Network;
import InfrastructureManager.Modules.NetworkStructure.NetworkModuleObject;
import InfrastructureManager.Modules.NetworkStructure.Exception.NetworkModuleException;

public class DistanceOutput extends NetworkModuleObject implements PlatformOutput{
	private Network network;
	public DistanceOutput(ImmutablePlatformModule module, String name,Network network) {
		super(module,name);
		this.network = network;
	}
	public void execute(String response) throws NetworkModuleException  {
		String[] commandLine = response.split(" ");
		if (commandLine[0].equals("distance")) {
			try {
				switch (commandLine[1]) {
				case "get_physical_distance_devices" -> {
					double distance =this.network.getphysicalDistanceBetweenDevices(commandLine[2],commandLine[3]);
					this.getSharedDistance().putValuetoQueue(distance);
				}
				case "get_physical_distance_application" -> {
					double distance =this.network.getphysicalDistanceBetweenApplications(commandLine[2],commandLine[3]);
					this.getSharedDistance().putValuetoQueue(distance);
				}
				case "get_network_distance" -> {
					double distance =this.network.getNetworkDistance(commandLine[2],commandLine[3]);
					this.getSharedDistance().putValuetoQueue(distance);
				}
				default -> throw new NetworkModuleException("Invalid command " + commandLine[1]
						+ " for NetworkModule");
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				throw new NetworkModuleException("Arguments missing for command " + response
						+ " for NetworkModule");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}