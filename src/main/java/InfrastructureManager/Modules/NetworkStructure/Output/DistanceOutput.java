package InfrastructureManager.Modules.NetworkStructure.Output;

import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;
import InfrastructureManager.ModuleManagement.PlatformOutput;
import InfrastructureManager.Modules.NetworkStructure.Network;
import InfrastructureManager.Modules.NetworkStructure.NetworkModuleObject;
import InfrastructureManager.Modules.NetworkStructure.Exception.NetworkModuleException;
import InfrastructureManager.Modules.NetworkStructure.Shared.SharedNetwork;

public class DistanceOutput extends NetworkModuleObject implements PlatformOutput{
	private final SharedNetwork sharednetwork;
	public DistanceOutput(ImmutablePlatformModule module, String name) {
		super(module,name);
		this.sharednetwork = getSharedNetwork();
	}
	public void execute(String response) throws NetworkModuleException  {
		String[] commandLine = response.split(" ");
		if (commandLine[0].equals("distance")) {
			try {
				switch (commandLine[1]) {
				case "physical" -> {
					switch(commandLine[2]) {
					case "devices"->{
						double distance =this.sharednetwork.computephysicalDistanceBetweenDevices(commandLine[3],commandLine[4]);
						this.getSharedDistance().putValuetoQueue(distance);
					}
					case "applications" ->{
						double distance =this.sharednetwork.computephysicalDistanceBetweenApplications(commandLine[3],commandLine[4]);
						this.getSharedDistance().putValuetoQueue(distance);
					}
					case "location" ->{ 
						double distance =this.sharednetwork.computephysicalDistanceBetweenlocations(commandLine[3],commandLine[4]);
						this.getSharedDistance().putValuetoQueue(distance);
					}
					default -> throw new NetworkModuleException("Invalid command " + commandLine[2]
							+ " for NetworkModule");
					}
				}
				case "network" -> {
					switch(commandLine[2]) {
					case "devices"->{ //ToDo implement
						
//						double distance =this.network.getNetworkDistance(commandLine[3],commandLine[4]);
//						this.getSharedDistance().putValuetoQueue(distance);
						double distance =0;
						this.getSharedDistance().putValuetoQueue(distance);
					}
					case "application" ->{//ToDo implement
						double distance =0;
						this.getSharedDistance().putValuetoQueue(distance);
					}
					case "location" ->{ //ToDo add distance between two locations implementation
						double distance =0;
						this.getSharedDistance().putValuetoQueue(distance);
					}
					default -> throw new NetworkModuleException("Invalid command " + commandLine[2]
							+ " for NetworkModule");
					}
				}
				default -> throw new NetworkModuleException("Invalid command " + commandLine[1]
						+ " for NetworkModule");
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				throw new NetworkModuleException("Arguments missing for command " + response
						+ " for NetworkModule");
			} catch (InterruptedException e) {
				throw new NetworkModuleException("Error while executing command " + response
						+ " for NetworkModule");
			}
		}

	}
}