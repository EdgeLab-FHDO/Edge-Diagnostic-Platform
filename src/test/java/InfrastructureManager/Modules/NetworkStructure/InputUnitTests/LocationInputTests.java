package InfrastructureManager.Modules.NetworkStructure.InputUnitTests;

import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleExecutionException;
import InfrastructureManager.Modules.NetworkStructure.Input.LocationInput;
import InfrastructureManager.Modules.NetworkStructure.Output.LocationOutput;
import InfrastructureManager.Modules.NetworkStructure.NetworkModule;
import InfrastructureManager.Modules.NetworkStructure.NetworkRequirements;
import InfrastructureManager.Modules.NetworkStructure.ApplicationInstance;
import InfrastructureManager.Modules.NetworkStructure.ApplicationInstanceDeviceRelation;
import InfrastructureManager.Modules.NetworkStructure.ApplicationType;
import InfrastructureManager.Modules.NetworkStructure.ComputeLimits;
import InfrastructureManager.Modules.NetworkStructure.ComputeRequirements;
import InfrastructureManager.Modules.NetworkStructure.Device;
import InfrastructureManager.Modules.NetworkStructure.DeviceLocationRelation;
import InfrastructureManager.Modules.NetworkStructure.DeviceType;
import InfrastructureManager.Modules.NetworkStructure.Location;
import InfrastructureManager.Modules.NetworkStructure.Network;
import org.junit.Assert;
import org.junit.Test;

public class LocationInputTests {

	DeviceType devicetype = new DeviceType(DeviceType.TypeId.UE,DeviceType.Mobility.NONMOVABLE,"Vodafone");
	ComputeLimits computelimits = new ComputeLimits(1,2,3);
	Device device1 = new Device("100","192.168.0.1",devicetype,computelimits);
	Location location1 = new Location("location1",15.5f,25.5f);
	ComputeRequirements computeRequirements = new ComputeRequirements(1.0f,2.0f,3.0f);
	NetworkRequirements networkRequirements = new NetworkRequirements(10.0f,20.0f,30.0,40.0);
	ApplicationType applicationType = new ApplicationType("Application1",computeRequirements,networkRequirements);
	ApplicationInstance application1 = new ApplicationInstance(applicationType,ApplicationInstance.State.RUNNING);
	ApplicationInstanceDeviceRelation applicationdevice = new ApplicationInstanceDeviceRelation(application1,device1);
	DeviceLocationRelation deviceLocation1 = new DeviceLocationRelation(device1,location1);
	Network network = updateNetwork();
	NetworkModule module = new NetworkModule();
	LocationOutput output = new LocationOutput(module,"location.out",network);
	LocationInput input = new LocationInput(module,"location.in");

	public Network updateNetwork() {
		Network network = new Network();
		network.updateDeviceLocationList(deviceLocation1, Network.Update.ADD);
		network.updateapplicationDeviceList(applicationdevice,Network.Update.ADD);
		return network;
	}

	@Test
	public void getDeviceLocationByID() throws ModuleExecutionException, InterruptedException {
		output.execute("location get_device_location 100");
		String expected = "location{\"locationId\":\"location1\",\"latitude\":15.5,\"longitude\":25.5}";
		Assert.assertEquals(expected, input.read());
	}

	@Test
	public void getApplicationLocationByID() throws ModuleExecutionException, InterruptedException {
		output.execute("location get_application_location Application1");
		String expected = "location{\"locationId\":\"location1\",\"latitude\":15.5,\"longitude\":25.5}";
		Assert.assertEquals(expected, input.read());
	}

}
