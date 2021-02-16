package InfrastructureManager.Modules.NetworkStructure.OutputUnitTests;

import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleExecutionException;
import InfrastructureManager.Modules.CommonTestingMethods;
import InfrastructureManager.Modules.NetworkStructure.Exception.NetworkModuleException;
import InfrastructureManager.Modules.NetworkStructure.SharedLocation;
import InfrastructureManager.Modules.NetworkStructure.Output.LocationOutput;
import InfrastructureManager.Modules.NetworkStructure.ComputeLimits;
import InfrastructureManager.Modules.NetworkStructure.Device;
import InfrastructureManager.Modules.NetworkStructure.DeviceLocationRelation;
import InfrastructureManager.Modules.NetworkStructure.DeviceType;
import InfrastructureManager.Modules.NetworkStructure.Location;
import InfrastructureManager.Modules.NetworkStructure.Network;
import InfrastructureManager.Modules.NetworkStructure.NetworkModule;
import org.junit.Assert;
import org.junit.Test;

public class LocationOutputTests {

	NetworkModule module = new NetworkModule();
	Network network = new Network();
	private final SharedLocation sharedLocation = module.getSharedLocation();



	private void assertExceptionInOutput(Class<? extends Exception> exceptionClass, String expectedMessage, String command) {
		LocationOutput output = new LocationOutput(module,"location.out",network);
		CommonTestingMethods.assertException(exceptionClass, expectedMessage, () -> output.execute(command));
	}

	@SuppressWarnings("deprecation")
	@Test
	public void deviceLocationverification() throws ModuleExecutionException {
		DeviceType devicetype = new DeviceType(DeviceType.TypeId.UE,DeviceType.Mobility.NONMOVABLE,"Vodafone");
		ComputeLimits computelimits = new ComputeLimits(1,2,3);
		Device device1 = new Device("100","192.168.0.1",devicetype,computelimits);
		Location location1 = new Location("location1",15.5f,25.5f);
		DeviceLocationRelation deviceLocation1 = new DeviceLocationRelation(device1,location1);
		network.updateDeviceLocationList(deviceLocation1, Network.Update.ADD);
		LocationOutput output = new LocationOutput(module,"location.out",network);
		output.execute("location get_device_location 100");
		double expectedLocationfordevice100 = 15.5f;
		Assert.assertEquals(expectedLocationfordevice100, sharedLocation.getLocationList().get("100").getLatitude(),0.5);
	}


	@Test
	public void invalidCommandThrowsException() {
		String command = "location notACommand";
		String expected = "Invalid command notACommand for NetworkModule";
		assertExceptionInOutput(NetworkModuleException.class, expected, command);
	}

	@Test
	public void incompleteCommandThrowsException() {
		String command = "location get_device_location";
		String expected = "Arguments missing for command " + command  + " for NetworkModule";
		assertExceptionInOutput(NetworkModuleException.class, expected, command);
	}

}
