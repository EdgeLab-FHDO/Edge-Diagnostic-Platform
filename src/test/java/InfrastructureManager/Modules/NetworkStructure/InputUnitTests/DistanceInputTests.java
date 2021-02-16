package InfrastructureManager.Modules.NetworkStructure.InputUnitTests;

import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleExecutionException;
import InfrastructureManager.Modules.NetworkStructure.Input.DistanceInput;
import InfrastructureManager.Modules.NetworkStructure.Output.DistanceOutput;
import InfrastructureManager.Modules.NetworkStructure.NetworkModule;
import InfrastructureManager.Modules.NetworkStructure.NetworkRequirements;
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
import InfrastructureManager.Modules.NetworkStructure.Network;
import InfrastructureManager.Modules.NetworkStructure.NetworkLimits;

import org.junit.Assert;
import org.junit.Test;

public class DistanceInputTests {

	NetworkModule module = new NetworkModule();
	Network network = new Network();


	@Test
	public void getDistanceBetweenDevices() throws ModuleExecutionException, InterruptedException {
		DeviceType devicetype = new DeviceType(DeviceType.TypeId.UE,DeviceType.Mobility.NONMOVABLE,"Vodafone");
		ComputeLimits computelimits = new ComputeLimits(1,2,3);
		Device device1 = new Device("100","192.168.0.1",devicetype,computelimits);
		Device device2 = new Device("200","192.169.0.1",devicetype,computelimits);
		Location location1 = new Location("location1",15.5f,25.5f);
		Location location2 = new Location("location2",35.5f,45.5f);
		DeviceLocationRelation deviceLocation1 = new DeviceLocationRelation(device1,location1);
		network.updateDeviceLocationList(deviceLocation1, Network.Update.ADD);
		DeviceLocationRelation deviceLocation2 = new DeviceLocationRelation(device2,location2);
		network.updateDeviceLocationList(deviceLocation2, Network.Update.ADD);
		DistanceOutput output = new DistanceOutput(module,"location.out",network);
		DistanceInput input = new DistanceInput(module,"distance.in",network);
		output.execute("distance get_physical_distance_devices 100 200");
		String expectedString = "set_distance {\"100200\":2982.9202837016414}";
		Assert.assertEquals(expectedString, input.read());
	}

	@Test
	public void getDistanceBetweenApplications() throws ModuleExecutionException, InterruptedException {
		DeviceType devicetype = new DeviceType(DeviceType.TypeId.UE,DeviceType.Mobility.NONMOVABLE,"Vodafone");
		ComputeLimits computelimits = new ComputeLimits(1,2,3);
		Device device1 = new Device("100","192.168.0.1",devicetype,computelimits);
		Location location1 = new Location("location1",15.5f,25.5f);
		Device device2 = new Device("200","192.169.0.1",devicetype,computelimits);
		Location location2 = new Location("location2",35.5f,45.5f);
		DeviceLocationRelation deviceLocation1 = new DeviceLocationRelation(device1,location1);
		DeviceLocationRelation deviceLocation2 = new DeviceLocationRelation(device2,location2);
		network.updateDeviceLocationList(deviceLocation1, Network.Update.ADD);
		network.updateDeviceLocationList(deviceLocation2, Network.Update.ADD);
		ComputeRequirements computeRequirements = new ComputeRequirements(1.0f,2.0f,3.0f);
		NetworkRequirements networkRequirements = new NetworkRequirements(10.0f,20.0f,30.0,40.0);
		ApplicationType applicationType1 = new ApplicationType("Application1",computeRequirements,networkRequirements);
		ApplicationType applicationType2 = new ApplicationType("Application2",computeRequirements,networkRequirements);
		ApplicationInstance application1 = new ApplicationInstance(applicationType1,ApplicationInstance.State.RUNNING);
		ApplicationInstance application2 = new ApplicationInstance(applicationType2,ApplicationInstance.State.RUNNING);
		ApplicationInstanceDeviceRelation applicationdevice1 = new ApplicationInstanceDeviceRelation(application1,device1);
		network.updateapplicationDeviceList(applicationdevice1,Network.Update.ADD);
		ApplicationInstanceDeviceRelation applicationdevice2 = new ApplicationInstanceDeviceRelation(application2,device2);
		network.updateapplicationDeviceList(applicationdevice2,Network.Update.ADD);
		DistanceOutput output = new DistanceOutput(module,"distance.out",network);
		DistanceInput input = new DistanceInput(module,"distance.in",network);
		output.execute("distance get_physical_distance_application Application1 Application2");
		String expected = "set_distance {\"Application1Application2\":2982.9202837016414}";
		Assert.assertEquals(expected, input.read());
	}

	@Test
	public void getNetworkDistance() throws ModuleExecutionException, InterruptedException {
		NetworkLimits networkLimits1 = new NetworkLimits(0.5f,0.2f,1.5,2.0);
		NetworkLimits networkLimits2 = new NetworkLimits(1.5f,1.2f,2.5,4.0);
		Connection connection1 = new Connection("Connection1",networkLimits1);
		Connection connection2 = new Connection("Connection2",networkLimits2);
		network.updateConnectionList(connection1, Network.Update.ADD);
		network.updateConnectionList(connection2, Network.Update.ADD);
		DistanceOutput output = new DistanceOutput(module,"location.out",network);
		DistanceInput input = new DistanceInput(module,"distance.in",network);
		output.execute("distance get_network_distance Connection1 Connection2");
		String expectedString = "set_distance {\"Connection1Connection2\":2.0}";
		Assert.assertEquals(expectedString, input.read());
	}

}
