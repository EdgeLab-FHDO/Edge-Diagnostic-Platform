package InfrastructureManager.Modules.NetworkStructure.Output;

import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;
import InfrastructureManager.ModuleManagement.PlatformOutput;
import InfrastructureManager.Modules.NetworkStructure.Network;
//import InfrastructureManager.Modules.NetworkStructure.SharedLocation;
import InfrastructureManager.Modules.NetworkStructure.Location;
import InfrastructureManager.Modules.NetworkStructure.NetworkModuleObject;
import InfrastructureManager.Modules.NetworkStructure.Exception.NetworkModuleException;

public class LocationOutput extends NetworkModuleObject implements PlatformOutput{
	private Network network;
	public LocationOutput(ImmutablePlatformModule module, String name,Network network) {
		super(module,name);
		this.network = network;
	}
	public void execute(String response) throws NetworkModuleException  {
		String[] commandLine = response.split(" ");
		if (commandLine[0].equals("location")) {
			try {
				switch (commandLine[1]) {
				case "get_device_location" -> {
					Location location =this.network.getCurrentDeviceLocation(commandLine[2]);
					this.getSharedLocation().putValuetoQueue(location);
				}
				case "get_application_location" -> {
					Location location =this.network.getCurrentApplicationLocation(commandLine[2]);
					this.getSharedLocation().putValuetoQueue(location);
				}
				default -> throw new NetworkModuleException("Invalid command " + commandLine[1]
						+ " for NetworkModule");
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				throw new NetworkModuleException("Arguments missing for command " + response
						+ " for NetworkModule");
			}
			catch (InterruptedException e) {
				throw new NetworkModuleException("Error while creating scenario " + response
						+ " for NetworkModule");
			}
		}

	}
}