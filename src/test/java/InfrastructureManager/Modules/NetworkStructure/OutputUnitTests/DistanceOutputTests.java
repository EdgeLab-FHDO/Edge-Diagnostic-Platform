package InfrastructureManager.Modules.NetworkStructure.OutputUnitTests;

import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleExecutionException;
import InfrastructureManager.Modules.CommonTestingMethods;
import InfrastructureManager.Modules.NetworkStructure.Exception.NetworkModuleException;
import InfrastructureManager.Modules.NetworkStructure.Shared.SharedDistance;
import InfrastructureManager.Modules.NetworkStructure.Output.DistanceOutput;
import InfrastructureManager.Modules.NetworkStructure.ComputeLimits;
import InfrastructureManager.Modules.NetworkStructure.Device;
import InfrastructureManager.Modules.NetworkStructure.DeviceLocationRelation;
import InfrastructureManager.Modules.NetworkStructure.DeviceType;
import InfrastructureManager.Modules.NetworkStructure.Location;
import InfrastructureManager.Modules.NetworkStructure.Network;
import InfrastructureManager.Modules.NetworkStructure.NetworkModule;
import org.junit.Assert;
import org.junit.Test;

public class DistanceOutputTests {
	
	DeviceType devicetype = new DeviceType(DeviceType.TypeId.UE,DeviceType.Mobility.NONMOVABLE,"Vodafone");
	ComputeLimits computelimits = new ComputeLimits(1,2,3);
	Device device1 = new Device("100","192.168.0.1",devicetype,computelimits);
	Device device2 = new Device("200","192.169.0.1",devicetype,computelimits);
	Location location1 = new Location("location1",15.5f,25.5f);
	Location location2 = new Location("location2",35.5f,45.5f);
	DeviceLocationRelation deviceLocation1 = new DeviceLocationRelation(device1,location1);
	DeviceLocationRelation deviceLocation2 = new DeviceLocationRelation(device2,location2);
	NetworkModule module = new NetworkModule();
	private final SharedDistance sharedDistance= module.getSharedDistance();
	Network network = updateNetwork();
	DistanceOutput output = new DistanceOutput(module,"location.out",network);


	private void assertExceptionInOutput(Class<? extends Exception> exceptionClass, String expectedMessage, String command) {
		DistanceOutput output = new DistanceOutput(module,"distance.out",network);
		CommonTestingMethods.assertException(exceptionClass, expectedMessage, () -> output.execute(command));
	}


	public Network updateNetwork() {
		Network network = new Network();
		network.updateDeviceLocationList(deviceLocation1, Network.Update.ADD);
		network.updateDeviceLocationList(deviceLocation2, Network.Update.ADD);
		return network;
	}

	@SuppressWarnings("deprecation")
	@Test
	public void deviceLocationverification() throws ModuleExecutionException, InterruptedException {
		output.execute("distance get_physical_distance_devices 100 200");
		String expectedDistance = "2982.9202837016414";
		Assert.assertEquals(expectedDistance, sharedDistance.getdistanceAsString());
	}


	@Test
	public void invalidCommandThrowsException() {
		String command = "distance notACommand";
		String expected = "Invalid command notACommand for NetworkModule";
		assertExceptionInOutput(NetworkModuleException.class, expected, command);
	}

	@Test
	public void incompleteCommandThrowsException() {
		String command = "distance get_physical_distance_devices";
		String expected = "Arguments missing for command " + command  + " for NetworkModule";
		assertExceptionInOutput(NetworkModuleException.class, expected, command);
	}

}
