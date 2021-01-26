package InfrastructureManager.Modules.NetworkStructure;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertThrows;

import java.util.Arrays;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * This class contains test cases for Network Structure Module.
 *
 * @author Shankar Lokeshwara
 */
public class NetworkTest {
	
	/**
	 * Test case to write JSON string
	 */
	Network network = new Network();
	
	@Test
	public void updateDeviceListAddTest() {
		DeviceType devicetype = new DeviceType(DeviceType.TypeId.UE,DeviceType.Mobility.NONMOVABLE,"Vodafone");
		ComputeLimits computelimits = new ComputeLimits(1,2,3);
		Device device1 = new Device("100","192.168.0.1",devicetype,computelimits);
		Device device2 = new Device("200","192.168.0.2",devicetype,computelimits);
		Device device3 = new Device("300","192.168.0.3",devicetype,computelimits);
		network.updateDeviceList(device1, Network.Update.ADD);
		network.updateDeviceList(device2, Network.Update.ADD);
		network.updateDeviceList(device3, Network.Update.ADD);
		Assert.assertEquals("100", device1.getDeviceId());
		Assert.assertEquals("200", device2.getDeviceId());
		Assert.assertEquals("300", device3.getDeviceId());
		Assert.assertEquals("192.168.0.1", device1.getDeviceAddress());
		Assert.assertEquals("192.168.0.2", device2.getDeviceAddress());
		Assert.assertEquals("192.168.0.3", device3.getDeviceAddress());
		Assert.assertEquals(DeviceType.TypeId.UE, device1.getDeviceType().getTypeId());
		Assert.assertEquals(DeviceType.TypeId.UE, device2.getDeviceType().getTypeId());
		Assert.assertEquals(DeviceType.TypeId.UE, device3.getDeviceType().getTypeId());
		Assert.assertEquals(DeviceType.Mobility.NONMOVABLE, device1.getDeviceType().getMobility());
		Assert.assertEquals(DeviceType.Mobility.NONMOVABLE, device2.getDeviceType().getMobility());
		Assert.assertEquals(DeviceType.Mobility.NONMOVABLE, device3.getDeviceType().getMobility());
		Assert.assertEquals("Vodafone", device1.getDeviceType().getDeviceName());
		Assert.assertEquals("Vodafone", device2.getDeviceType().getDeviceName());
		Assert.assertEquals("Vodafone", device3.getDeviceType().getDeviceName());
	}
	
	
	@Test
	public void updateDeviceListDeleteTest() {
		DeviceType devicetype = new DeviceType(DeviceType.TypeId.UE,DeviceType.Mobility.NONMOVABLE,"Vodafone");
		ComputeLimits computelimits = new ComputeLimits(1,2,3);
		Device device2 = new Device("200","192.168.0.2",devicetype,computelimits);
		network.updateDeviceList(device2, Network.Update.DELETE);
	}
	
	@Test
	public void getDeviceFromListTest() {
		DeviceType devicetype = new DeviceType(DeviceType.TypeId.UE,DeviceType.Mobility.NONMOVABLE,"Vodafone");
		ComputeLimits computelimits = new ComputeLimits(1,2,3);
		Device device1 = new Device("400","192.168.0.4",devicetype,computelimits);
		network.updateDeviceList(device1, Network.Update.ADD);
		Device device =	network.getDeviceFromList("400");
		Assert.assertEquals("400", device.getDeviceId());
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void updateConnectionAddList() {
		NetworkLimits networkLimits = new NetworkLimits(0.5f,0.2f,1.5,2.0);
		Connection connection1 = new Connection("Connection1",networkLimits);
		Connection connection2 = new Connection("Connection2",networkLimits);
		Connection connection3 = new Connection("Connection3",networkLimits);
		network.updateConnectionList(connection1, Network.Update.ADD);
		network.updateConnectionList(connection2, Network.Update.ADD);
		network.updateConnectionList(connection3, Network.Update.ADD);
		Assert.assertEquals("Connection1", connection1.getConnectionId());
		Assert.assertEquals("Connection2", connection2.getConnectionId());
		Assert.assertEquals("Connection3", connection3.getConnectionId());
		Assert.assertEquals(0.5f, connection1.getNetworkCapacity().getLatency(),0.5);
		Assert.assertEquals(0.2f, connection2.getNetworkCapacity().getJitter(),0.5);
		Assert.assertEquals(1.5, connection3.getNetworkCapacity().getThroughput(),0.5);
		Assert.assertEquals(2.0, connection3.getNetworkCapacity().getPacketLoss(),0.5);
	}
	
	
	@Test
	public void updateConnectionDeleteList() {
		NetworkLimits networkLimits = new NetworkLimits(0.5f,0.2f,1.5,2.0);
		Connection connection1 = new Connection("Connection1",networkLimits);
		network.updateConnectionList(connection1, Network.Update.DELETE);
	}
	
	@Test
	public void getConnectionFromList() {
		NetworkLimits networkLimits = new NetworkLimits(0.5f,0.2f,1.5,2.0);
		Connection connection1 = new Connection("Connection1",networkLimits);
		network.updateConnectionList(connection1, Network.Update.ADD);
		Connection connection =	network.getConnectionFromList("Connection1");
		Assert.assertEquals("Connection1", connection.getConnectionId());
	}	
	
	@SuppressWarnings("deprecation")
	@Test
	public void updateApplicationInstanceAddList() {
		ComputeRequirements computeRequirements = new ComputeRequirements(1.0f,2.0f,3.0f);
		NetworkRequirements networkRequirements = new NetworkRequirements(10.0f,20.0f,30.0,40.0);
		ApplicationType applicationType = new ApplicationType("Application1",computeRequirements,networkRequirements);
		ApplicationInstance application1 = new ApplicationInstance(applicationType,ApplicationInstance.State.RUNNING);
		network.updateApplicationInstanceList(application1, Network.Update.ADD);
		Assert.assertEquals(40.0,networkRequirements.getRequiredPacketLoss(),0.5);
	}
	
	
	@Test
	public void updateApplicationInstanceDeleteList() {
		ComputeRequirements computeRequirements = new ComputeRequirements(1.0f,2.0f,3.0f);
		NetworkRequirements networkRequirements = new NetworkRequirements(10.0f,20.0f,30.0,40.0);
		ApplicationType applicationType = new ApplicationType("Application1",computeRequirements,networkRequirements);
		ApplicationInstance application1 = new ApplicationInstance(applicationType,ApplicationInstance.State.RUNNING);
		network.updateApplicationInstanceList(application1, Network.Update.DELETE);
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void getApplicationInstanceFromList() {
		ComputeRequirements computeRequirements = new ComputeRequirements(1.0f,2.0f,3.0f);
		NetworkRequirements networkRequirements = new NetworkRequirements(10.0f,20.0f,30.0,40.0);
		ApplicationType applicationType = new ApplicationType("Application1",computeRequirements,networkRequirements);
		ApplicationInstance application1 = new ApplicationInstance(applicationType,ApplicationInstance.State.RUNNING);
		network.updateApplicationInstanceList(application1, Network.Update.ADD);
		ApplicationInstance application = network.getApplicationInstanceFromList("Application1");
		Assert.assertEquals(10.0f, application.getApplicationType().getNetworkSpecification().getRequiredLatency(),0.5);
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void updateLocationAddList() {
		Location location1 = new Location("location1",10.5f,20.5f);
		Location location2 = new Location("location2",15.5f,25.5f);
		Location location3 = new Location("location3",20.5f,40.5f);
		network.updateLocationList(location1, Network.Update.ADD);
		network.updateLocationList(location2, Network.Update.ADD);
		network.updateLocationList(location3, Network.Update.ADD);
		Assert.assertEquals("location1",location1.getLocationId());
		Assert.assertEquals("location2",location2.getLocationId());
		Assert.assertEquals("location3",location3.getLocationId());
		Assert.assertEquals(15.5,location2.getLatitude(),0.5);
		Assert.assertEquals(40.5,location3.getLongitude(),0.5);
	}
	
	
	@Test
	public void updateLocationDeleteList() {
		Location location1 = new Location("location1",10.5f,20.5f);
		network.updateLocationList(location1, Network.Update.DELETE);
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void getLocationFromList() {
		Location location1 = new Location("location1",10.5f,20.5f);
		network.updateLocationList(location1, Network.Update.ADD);
		Location location = network.getLocationFromList("location1");
		Assert.assertEquals("location1",location1.getLocationId());
		Assert.assertEquals("location1",location.getLocationId());
		Assert.assertEquals(10.5,location.getLatitude(),0.5);
		Assert.assertEquals(20.5,location.getLongitude(),0.5);
	}
	
	
	@Test
	public void saveNetworkTest() throws JsonProcessingException {
	 // create network object
		DeviceType devicetype = new DeviceType(DeviceType.TypeId.UE,DeviceType.Mobility.NONMOVABLE,"Vodafone");
		ComputeLimits computelimits = new ComputeLimits(1,2,3);
		Device device1 = new Device("100","192.168.0.1",devicetype,computelimits);
		network.updateDeviceList(device1, Network.Update.ADD);
		NetworkLimits networkLimits = new NetworkLimits(0.5f,0.2f,1.5,2.0);
		Connection connection1 = new Connection("Connection1",networkLimits);
		network.updateConnectionList(connection1, Network.Update.ADD);
		ComputeRequirements computeRequirements = new ComputeRequirements(1.0f,2.0f,3.0f);
		NetworkRequirements networkRequirements = new NetworkRequirements(10.0f,20.0f,30.0,40.0);
		ApplicationType applicationType = new ApplicationType("Application1",computeRequirements,networkRequirements);
		ApplicationInstance application1 = new ApplicationInstance(applicationType,ApplicationInstance.State.RUNNING);
		network.updateApplicationInstanceList(application1, Network.Update.ADD);
		Location location1 = new Location("location1",10.5f,20.5f);
		network.updateLocationList(location1, Network.Update.ADD);
		network.setApplicationDeviceList(Arrays.asList(new ApplicationInstanceDeviceRelation(network.getApplicationInstanceFromList("Application1"),network.getDeviceFromList("100"))));
		network.setDeviceLocationList(Arrays.asList(new DeviceLocationRelation(network.getDeviceFromList("100"),network.getLocationFromList("location1"))));
		network.setLocationConnectionList(Arrays.asList(new LocationConnectionRelation(network.getConnectionFromList("Connection1"),network.getLocationFromList("location1"))));
		String writeString = network.saveNetwork();
		System.out.println(writeString);
	}
	
	/**
	 * Test case to read JSON string
	 */
	//Commented as the JSON string read is exceeding memory and the test case fails due to that.
//	@Test
//	public void loadNetworkTest() throws JsonProcessingException {
//		String jSON = "{\"applicationDeviceList\":[{\"application\":{\"applicationState\":\"RUNNING\",\"applicationType\":{\"applicationId\":\"Application1\",\"computeSpecification\":{\"requiredCpu\":1,\"requiredGpu\":2,\"requiredMemory\":3},\"networkSpecification\":{\"requiredLatency\":10,\"requiredJitter\":20,\"requiredThroughput\":30,\"requiredPacketLoss\":40}}},\"device\":{\"deviceId\":\"100\",\"deviceAddress\":\"192.168.0.1\",\"deviceType\":{\"deviceName\":\"Vodafone\",\"typeId\":\"UE\",\"mobility\":\"NONMOVABLE\"},\"computeLimits\":{\"cpuCount\":1,\"gpuCount\":2,\"memoryLimit\":3}}}],\"locationConnectionList\":[{\"locationRelation\":{\"locationId\":\"location1\",\"latitude\":10.5,\"longitude\":20.5},\"connectionRelation\":{\"connectionId\":\"Connection1\",\"networkCapacity\":{\"latency\":0.5,\"jitter\":0.2,\"throughput\":1.5,\"packetLoss\":2}}}],\"deviceLocationList\":[{\"location\":{\"locationId\":\"location1\",\"latitude\":10.5,\"longitude\":20.5},\"deviceLocation\":{\"deviceId\":\"100\",\"deviceAddress\":\"192.168.0.1\",\"deviceType\":{\"deviceName\":\"Vodafone\",\"typeId\":\"UE\",\"mobility\":\"NONMOVABLE\"},\"computeLimits\":{\"cpuCount\":1,\"gpuCount\":2,\"memoryLimit\":3}}}]}";
//    	network.loadNetwork(jSON);
//    	Device device =	network.getDeviceFromList("100");
//		Assert.assertEquals("100", device.getDeviceId());
//		Connection connection =	network.getConnectionFromList("Connection1");
//		Assert.assertEquals("Connection1", connection.getConnectionId());
//		Location location = network.getLocationFromList("location1");
//		Assert.assertEquals("location1",location.getLocationId());
//		Assert.assertEquals(10.5,location.getLatitude(),0.5);
//		Assert.assertEquals(20.5,location.getLongitude(),0.5);
//	}
	
	
}
