package InfrastructureManager.Modules.NetworkStructure.Output;

import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;
import InfrastructureManager.ModuleManagement.PlatformOutput;
import InfrastructureManager.Modules.NetworkStructure.Network;
//import InfrastructureManager.Modules.NetworkStructure.SharedLocation;
import InfrastructureManager.Modules.NetworkStructure.Location;
import InfrastructureManager.Modules.NetworkStructure.NetworkModuleObject;
import InfrastructureManager.Modules.NetworkStructure.Exception.NetworkModuleException;
import InfrastructureManager.Modules.NetworkStructure.Shared.SharedNetwork;

public class LocationOutput extends NetworkModuleObject implements PlatformOutput{
	private final SharedNetwork sharednetwork;
	public LocationOutput(ImmutablePlatformModule module, String name) {
		super(module,name);
		this.sharednetwork = getSharedNetwork();
	}
	public void execute(String response) throws NetworkModuleException  {
		String[] commandLine = response.split(" ");
		if (commandLine[0].equals("location")) {
			try {
				switch (commandLine[1]) {
				case "device" -> {
					Location location =this.sharednetwork.computeCurrentDeviceLocation(commandLine[2]);
					this.getSharedLocation().putValuetoQueue(location);
				}
				case "application" -> {
					Location location =this.sharednetwork.computeCurrentApplicationLocation(commandLine[2]);
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