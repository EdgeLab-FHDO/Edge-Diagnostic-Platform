package InfrastructureManager.Modules.NetworkStructure.OutputUnitTests;

import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleExecutionException;
import InfrastructureManager.Modules.CommonTestingMethods;
import InfrastructureManager.Modules.NetworkStructure.Exception.NetworkModuleException;
import InfrastructureManager.Modules.NetworkStructure.Shared.SharedDistance;
import InfrastructureManager.Modules.NetworkStructure.Shared.SharedNetwork;
import InfrastructureManager.Modules.NetworkStructure.Output.DistanceOutput;
import InfrastructureManager.Modules.NetworkStructure.ApplicationInstance;
import InfrastructureManager.Modules.NetworkStructure.ApplicationInstanceDeviceRelation;
import InfrastructureManager.Modules.NetworkStructure.ApplicationType;
import InfrastructureManager.Modules.NetworkStructure.ComputeLimits;
import InfrastructureManager.Modules.NetworkStructure.ComputeRequirements;
import InfrastructureManager.Modules.NetworkStructure.Connection;
import InfrastructureManager.Modules.NetworkStructure.Device;
import InfrastructureManager.Modules.NetworkStructure.DeviceLocationRelation;
import InfrastructureManager.Modules.NetworkStructure.DeviceType;
import InfrastructureManager.Modules.NetworkStructure.Location;
import InfrastructureManager.Modules.NetworkStructure.LocationConnectionRelation;
import InfrastructureManager.Modules.NetworkStructure.Network;
import InfrastructureManager.Modules.NetworkStructure.NetworkLimits;
import InfrastructureManager.Modules.NetworkStructure.NetworkModule;
import InfrastructureManager.Modules.NetworkStructure.NetworkRequirements;

import org.junit.Assert;
import org.junit.Test;

public class DistanceOutputTests {
	NetworkModule module = new NetworkModule();
	private final SharedDistance sharedDistance= module.getSharedDistance();
	private final SharedNetwork sharedNetwork= module.getSharedNetwork();
	Network network = new Network();
	DistanceOutput output = new DistanceOutput(module,"distance.out");

	private void assertExceptionInOutput(Class<? extends Exception> exceptionClass, String expectedMessage, String command) {
		DistanceOutput output = new DistanceOutput(module,"distance.out");
		CommonTestingMethods.assertException(exceptionClass, expectedMessage, () -> output.execute(command));
	}


	public void updateNetwork() {
		DeviceType devicetype = new DeviceType(DeviceType.TypeId.UE,DeviceType.Mobility.NONMOVABLE,"Vodafone");
		ComputeLimits computelimits = new ComputeLimits(1,2,3);
		Device device1 = new Device("100","192.168.0.1",devicetype,computelimits);
		Device device2 = new Device("200","192.169.0.1",devicetype,computelimits);
		Location location1 = new Location("location1",15.5f,25.5f);
		Location location2 = new Location("location2",35.5f,45.5f);
		DeviceLocationRelation deviceLocation1 = new DeviceLocationRelation("devLoc1",device1,location1);
		DeviceLocationRelation deviceLocation2 = new DeviceLocationRelation("devLoc2",device2,location2);
		ComputeRequirements computeRequirements = new ComputeRequirements(1.0f,2.0f,3.0f);
		NetworkRequirements networkRequirements = new NetworkRequirements(10.0f,20.0f,30.0,40.0);
		ApplicationType applicationType1 = new ApplicationType("Application1",computeRequirements,networkRequirements);
		ApplicationType applicationType2 = new ApplicationType("Application2",computeRequirements,networkRequirements);
		ApplicationInstance application1 = new ApplicationInstance(applicationType1,ApplicationInstance.State.RUNNING);
		ApplicationInstance application2 = new ApplicationInstance(applicationType2,ApplicationInstance.State.RUNNING);
		ApplicationInstanceDeviceRelation applicationdevice1 = new ApplicationInstanceDeviceRelation("appDev1",application1,device1);
		ApplicationInstanceDeviceRelation applicationdevice2 = new ApplicationInstanceDeviceRelation("appDev2",application2,device2);
		NetworkLimits networkLimits1 = new NetworkLimits(0.5f,0.2f,1.5,2.0);
		NetworkLimits networkLimits2 = new NetworkLimits(1.5f,1.2f,2.5,4.0);
		Connection connection1 = new Connection("Connection1",networkLimits1);
		Connection connection2 = new Connection("Connection2",networkLimits2);
		LocationConnectionRelation locCon1 = new LocationConnectionRelation("locCon1",location1,connection1);
		LocationConnectionRelation locCon2 = new LocationConnectionRelation("locCon2",location2,connection2);
		network.addDevice(device1);
		network.addDevice(device2);
		network.addLocation(location1);
		network.addLocation(location2);
		network.addConnection(connection1);
		network.addConnection(connection2);
		network.addApplicationInstance(application1);
		network.addApplicationInstance(application2);
		network.addDeviceLocationRelation(deviceLocation1);
		network.addDeviceLocationRelation(deviceLocation2);
		network.addApplicationDeviceRelation(applicationdevice1);
		network.addApplicationDeviceRelation(applicationdevice2);
		network.addLocationConnectionRelation(locCon1);
		network.addLocationConnectionRelation(locCon2);
	}

	@Test
	public void physicalDistanceBetweenDevicesTest() throws ModuleExecutionException, InterruptedException {
		updateNetwork();
		sharedNetwork.UpdateSharedNetworkObject(network);
		output.execute("distance physical devices 100 200");
		String expectedDistance = "2982.9202837016414";
		Assert.assertEquals(expectedDistance, sharedDistance.getdistanceAsString());
	}
	
	@Test
	public void physicalDistanceBetweenApplicationTest() throws ModuleExecutionException, InterruptedException {
		updateNetwork();
		sharedNetwork.UpdateSharedNetworkObject(network);
		output.execute("distance physical applications Application1 Application2");
		String expectedDistance = "2982.9202837016414";
		Assert.assertEquals(expectedDistance, sharedDistance.getdistanceAsString());
	}

	@Test
	public void physicalDistanceBetweenLocationTest() throws ModuleExecutionException, InterruptedException {
		updateNetwork();
		sharedNetwork.UpdateSharedNetworkObject(network);
		output.execute("distance physical location location1 location2");
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
		String command = "distance physical";
		String expected = "Arguments missing for command " + command  + " for NetworkModule";
		assertExceptionInOutput(NetworkModuleException.class, expected, command);
	}

}
