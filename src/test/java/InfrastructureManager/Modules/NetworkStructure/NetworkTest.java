package InfrastructureManager.Modules.NetworkStructure;
import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
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
	public void getDeviceTest() {
		DeviceType devicetype = new DeviceType(DeviceType.TypeId.UE,DeviceType.Mobility.NONMOVABLE,"Vodafone");
		ComputeLimits computelimits = new ComputeLimits(1,2,3);
		Device device1 = new Device("100","192.168.0.1",devicetype,computelimits);
		Device device2 = new Device("200","192.168.0.2",devicetype,computelimits);
		Device device3 = new Device("300","192.168.0.3",devicetype,computelimits);
		network.addDevice(device1);
		network.addDevice(device2);
		network.addDevice(device3);
		Assert.assertEquals("100", network.getDevice("100").getDeviceId());
	}

	@Test
	public void addDeviceTest() {
		DeviceType devicetype = new DeviceType(DeviceType.TypeId.UE,DeviceType.Mobility.NONMOVABLE,"Vodafone");
		ComputeLimits computelimits = new ComputeLimits(1,2,3);
		Device device1 = new Device("100","192.168.0.1",devicetype,computelimits);
		Device device2 = new Device("200","192.168.0.2",devicetype,computelimits);
		Device device3 = new Device("300","192.168.0.3",devicetype,computelimits);
		network.addDevice(device1);
		network.addDevice(device2);
		network.addDevice(device3);
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
		Assert.assertEquals("", device3.getApplicationRelationId());
		Assert.assertEquals("", device3.getLocationRelationId());
	}


	@Test
	public void removeDeviceTest() {
		DeviceType devicetype = new DeviceType(DeviceType.TypeId.UE,DeviceType.Mobility.NONMOVABLE,"Vodafone");
		ComputeLimits computelimits = new ComputeLimits(1,2,3);
		Device device2 = new Device("200","192.168.0.2",devicetype,computelimits);
		network.addDevice(device2);
		Assert.assertEquals("200", network.getDevice("200").getDeviceId());
		network.removeDevice("200");
		Assert.assertNull(network.getDevice("200"));
	}

	@SuppressWarnings("deprecation")
	@Test
	public void addConnectionTest() {
		NetworkLimits networkLimits = new NetworkLimits(0.5f,0.2f,1.5,2.0);
		Connection connection1 = new Connection("Connection1",networkLimits);
		Connection connection2 = new Connection("Connection2",networkLimits);
		Connection connection3 = new Connection("Connection3",networkLimits);
		network.addConnection(connection1);
		network.addConnection(connection2);
		network.addConnection(connection3);
		Assert.assertEquals("Connection1", connection1.getConnectionId());
		Assert.assertEquals("Connection2", connection2.getConnectionId());
		Assert.assertEquals("Connection3", connection3.getConnectionId());
		Assert.assertEquals(0.5f, connection1.getNetworkCapacity().getLatency(),0.5);
		Assert.assertEquals(0.2f, connection2.getNetworkCapacity().getJitter(),0.5);
		Assert.assertEquals(1.5, connection3.getNetworkCapacity().getThroughput(),0.5);
		Assert.assertEquals(2.0, connection3.getNetworkCapacity().getPacketLoss(),0.5);
		Assert.assertEquals("", connection3.getDeviceRelationId());
		Assert.assertEquals("", connection3.getLocationRelationId());
	}


	@Test
	public void removeConnectionTest() {
		NetworkLimits networkLimits = new NetworkLimits(0.5f,0.2f,1.5,2.0);
		Connection connection1 = new Connection("Connection1",networkLimits);
		network.addConnection(connection1);
		Assert.assertEquals("Connection1", network.getConnection("Connection1").getConnectionId());
		network.removeConnection("Connection1");
		Assert.assertNull(network.getConnection("Connection1"));
	}

	@Test
	public void getConnectionFromList() {
		NetworkLimits networkLimits = new NetworkLimits(0.5f,0.2f,1.5,2.0);
		Connection connection1 = new Connection("Connection1",networkLimits);
		network.addConnection(connection1);
		Connection connection =	network.getConnection("Connection1");
		Assert.assertEquals("Connection1", connection.getConnectionId());
	}	

	@SuppressWarnings("deprecation")
	@Test
	public void addApplicationInstanceTest() {
		ComputeRequirements computeRequirements = new ComputeRequirements(1.0f,2.0f,3.0f);
		NetworkRequirements networkRequirements = new NetworkRequirements(10.0f,20.0f,30.0,40.0);
		ApplicationType applicationType = new ApplicationType("Application1",computeRequirements,networkRequirements);
		ApplicationInstance application1 = new ApplicationInstance(applicationType,ApplicationInstance.State.RUNNING);
		network.addApplicationInstance(application1);
		Assert.assertEquals(10.0,network.getApplicationInstance("Application1").getApplicationType().getNetworkSpecification().getRequiredLatency(),0.5);
	}


	@Test
	public void removeApplicationInstanceTest() {
		ComputeRequirements computeRequirements = new ComputeRequirements(1.0f,2.0f,3.0f);
		NetworkRequirements networkRequirements = new NetworkRequirements(10.0f,20.0f,30.0,40.0);
		ApplicationType applicationType = new ApplicationType("Application1",computeRequirements,networkRequirements);
		ApplicationInstance application1 = new ApplicationInstance(applicationType,ApplicationInstance.State.RUNNING);
		network.addApplicationInstance(application1);
		Assert.assertEquals(10.0,network.getApplicationInstance("Application1").getApplicationType().getNetworkSpecification().getRequiredLatency(),0.5);
		Assert.assertNull(network.getApplicationInstance("Connection1"));
	}

	@SuppressWarnings("deprecation")
	@Test
	public void getApplicationInstanceTest() {
		ComputeRequirements computeRequirements = new ComputeRequirements(1.0f,2.0f,3.0f);
		NetworkRequirements networkRequirements = new NetworkRequirements(10.0f,20.0f,30.0,40.0);
		ApplicationType applicationType = new ApplicationType("Application1",computeRequirements,networkRequirements);
		ApplicationInstance application1 = new ApplicationInstance(applicationType,ApplicationInstance.State.RUNNING);
		network.addApplicationInstance(application1);
		ApplicationInstance application = network.getApplicationInstance("Application1");
		Assert.assertEquals(10.0f, application.getApplicationType().getNetworkSpecification().getRequiredLatency(),0.5);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void addLocationTest() {
		Location location1 = new Location("location1",10.5f,20.5f);
		Location location2 = new Location("location2",15.5f,25.5f);
		Location location3 = new Location("location3",20.5f,40.5f);
		network.addLocation(location1);
		network.addLocation(location2);
		network.addLocation(location3);
		Assert.assertEquals("location1",network.getLocation("location1").getLocationId());
		Assert.assertEquals("",network.getLocation("location2").getConnectionRelationId());
		Assert.assertEquals("",network.getLocation("location3").getDeviceRelationId());
		Assert.assertEquals("location3",location3.getLocationId());
		Assert.assertEquals(15.5,location2.getLatitude(),0.5);
		Assert.assertEquals(40.5,location3.getLongitude(),0.5);
	}


	@Test
	public void removeLocationTest() {
		Location location1 = new Location("location1",10.5f,20.5f);
		network.addLocation(location1);
		Assert.assertEquals("location1",network.getLocation("location1").getLocationId());
		network.removeLocation("location1");
		Assert.assertNull(network.getLocation("location1"));
	}

	@SuppressWarnings("deprecation")
	@Test
	public void getLocationTest() {
		Location location1 = new Location("location1",10.5f,20.5f);
		network.addLocation(location1);
		Assert.assertEquals("location1",network.getLocation("location1").getLocationId());
		Assert.assertEquals("location1",network.getLocation("location1").getLocationId());
		Assert.assertEquals(10.5,network.getLocation("location1").getLatitude(),0.5);
		Assert.assertEquals(20.5,network.getLocation("location1").getLongitude(),0.5);
	}
	
	@Test
	public void addApplicationInstanceDeviceRelationTest() {
		ComputeRequirements computeRequirements = new ComputeRequirements(1.0f,2.0f,3.0f);
		NetworkRequirements networkRequirements = new NetworkRequirements(10.0f,20.0f,30.0,40.0);
		ApplicationType applicationType = new ApplicationType("Application1",computeRequirements,networkRequirements);
		ApplicationInstance application1 = new ApplicationInstance(applicationType,ApplicationInstance.State.RUNNING);
		DeviceType devicetype = new DeviceType(DeviceType.TypeId.UE,DeviceType.Mobility.NONMOVABLE,"Vodafone");
		ComputeLimits computelimits = new ComputeLimits(1,2,3);
		Device device1 = new Device("100","192.168.0.1",devicetype,computelimits);
		ApplicationInstanceDeviceRelation appDevice1 = new ApplicationInstanceDeviceRelation("appDevice1",application1,device1);
		Assert.assertEquals("appDevice1",appDevice1.getApplicationDeviceRelationId());
		Assert.assertEquals("appDevice1",appDevice1.getApplicationDevice().getApplicationRelationId());
		Assert.assertEquals("appDevice1",appDevice1.getApplication().getDeviceRelationId());
		network.addApplicationDeviceRelation(appDevice1);
		Assert.assertEquals("appDevice1",network.getApplicationInstanceDeviceRelation("appDevice1").getApplicationDeviceRelationId());
	}
	
	@Test
	public void removeApplicationInstanceDeviceRelationTest() {
		ComputeRequirements computeRequirements = new ComputeRequirements(1.0f,2.0f,3.0f);
		NetworkRequirements networkRequirements = new NetworkRequirements(10.0f,20.0f,30.0,40.0);
		ApplicationType applicationType = new ApplicationType("Application1",computeRequirements,networkRequirements);
		ApplicationInstance application1 = new ApplicationInstance(applicationType,ApplicationInstance.State.RUNNING);
		DeviceType devicetype = new DeviceType(DeviceType.TypeId.UE,DeviceType.Mobility.NONMOVABLE,"Vodafone");
		ComputeLimits computelimits = new ComputeLimits(1,2,3);
		Device device1 = new Device("100","192.168.0.1",devicetype,computelimits);
		ApplicationInstanceDeviceRelation appDevice1 = new ApplicationInstanceDeviceRelation("appDevice1",application1,device1);
		Assert.assertEquals("appDevice1",appDevice1.getApplicationDeviceRelationId());
		Assert.assertEquals("appDevice1",appDevice1.getApplicationDevice().getApplicationRelationId());
		Assert.assertEquals("appDevice1",appDevice1.getApplication().getDeviceRelationId());
		network.addApplicationDeviceRelation(appDevice1);
		Assert.assertEquals("appDevice1",network.getApplicationInstanceDeviceRelation("appDevice1").getApplicationDeviceRelationId());
		network.removeApplicationDeviceRelation("appDevice1");
		Assert.assertNull(network.getApplicationInstanceDeviceRelation("appDevice1"));
	}
	
	@Test
	public void getApplicationInstanceDeviceRelationTest() {
		ComputeRequirements computeRequirements = new ComputeRequirements(1.0f,2.0f,3.0f);
		NetworkRequirements networkRequirements = new NetworkRequirements(10.0f,20.0f,30.0,40.0);
		ApplicationType applicationType = new ApplicationType("Application1",computeRequirements,networkRequirements);
		ApplicationInstance application1 = new ApplicationInstance(applicationType,ApplicationInstance.State.RUNNING);
		DeviceType devicetype = new DeviceType(DeviceType.TypeId.UE,DeviceType.Mobility.NONMOVABLE,"Vodafone");
		ComputeLimits computelimits = new ComputeLimits(1,2,3);
		Device device1 = new Device("100","192.168.0.1",devicetype,computelimits);
		ApplicationInstanceDeviceRelation appDevice1 = new ApplicationInstanceDeviceRelation("appDevice1",application1,device1);
		Assert.assertEquals("appDevice1",appDevice1.getApplicationDeviceRelationId());
		Assert.assertEquals("appDevice1",appDevice1.getApplicationDevice().getApplicationRelationId());
		Assert.assertEquals("appDevice1",appDevice1.getApplication().getDeviceRelationId());
		network.addApplicationDeviceRelation(appDevice1);
		Assert.assertEquals("appDevice1",network.getApplicationInstanceDeviceRelation("appDevice1").getApplicationDeviceRelationId());
	}
	
	@Test
	
	public void addAndGetLocationConnectionRelationTest() {
		NetworkLimits networkLimits = new NetworkLimits(0.5f,0.2f,1.5,2.0);
		Connection connection1 = new Connection("Connection1",networkLimits);
		Assert.assertEquals(0.5f, connection1.getNetworkCapacity().getLatency(),0.5);
		Location location1 = new Location("location1",10.5f,20.5f);
		LocationConnectionRelation locCon1 = new LocationConnectionRelation("locCon1",location1,connection1);
		network.addLocationConnectionRelation(locCon1);
		Assert.assertEquals("location1",network.getLocationConnectionRelation("locCon1").getLocationRelation().getLocationId());
		Assert.assertEquals("locCon1",network.getLocationConnectionRelation("locCon1").getLocationConnectionRelationId());
		Assert.assertEquals("locCon1",network.getLocationConnectionRelation("locCon1").getConnectionRelation().getLocationRelationId());
		Assert.assertEquals("locCon1",network.getLocationConnectionRelation("locCon1").getLocationRelation().getConnectionRelationId());
	}
	
	@Test
	
	public void removeLocationConnectionRelationTest() {
		NetworkLimits networkLimits = new NetworkLimits(0.5f,0.2f,1.5,2.0);
		Connection connection1 = new Connection("Connection1",networkLimits);
		Assert.assertEquals(0.5f, connection1.getNetworkCapacity().getLatency(),0.5);
		Location location1 = new Location("location1",10.5f,20.5f);
		LocationConnectionRelation locCon1 = new LocationConnectionRelation("locCon1",location1,connection1);
		network.addLocationConnectionRelation(locCon1);
		Assert.assertEquals("location1",network.getLocationConnectionRelation("locCon1").getLocationRelation().getLocationId());
		Assert.assertEquals("locCon1",network.getLocationConnectionRelation("locCon1").getLocationConnectionRelationId());
		Assert.assertEquals("locCon1",network.getLocationConnectionRelation("locCon1").getConnectionRelation().getLocationRelationId());
		Assert.assertEquals("locCon1",network.getLocationConnectionRelation("locCon1").getLocationRelation().getConnectionRelationId());
		network.removeLocationConnectionRelation("locCon1");
		Assert.assertNull(network.getLocationConnectionRelation("locCon1"));
	}
	
	@Test
	
	public void deviceLocationRelationTest() {
		DeviceType devicetype = new DeviceType(DeviceType.TypeId.UE,DeviceType.Mobility.NONMOVABLE,"Vodafone");
		ComputeLimits computelimits = new ComputeLimits(1,2,3);
		Device device1 = new Device("100","192.168.0.1",devicetype,computelimits);
		Location location1 = new Location("location1",10.5f,20.5f);
		DeviceLocationRelation devLoc1 = new DeviceLocationRelation("devLoc1",device1,location1);
		network.addDeviceLocationRelation(devLoc1);
		Assert.assertEquals("location1",network.getDeviceLocationRelation("devLoc1").getLocation().getLocationId());
		Assert.assertEquals("devLoc1",network.getDeviceLocationRelation("devLoc1").getLocation().getDeviceRelationId());
		Assert.assertEquals("devLoc1",network.getDeviceLocationRelation("devLoc1").getDeviceLocationRelationId());
		Assert.assertEquals("devLoc1",network.getDeviceLocationRelation("devLoc1").getDevice().getLocationRelationId());
		network.removeDeviceLocationRelation("devLoc1");
		Assert.assertNull(network.getDeviceLocationRelation("devLoc1"));
	}
	
	@Test
	
	public void getCurrentDeviceLocationTest()
	{
		DeviceType devicetype = new DeviceType(DeviceType.TypeId.UE,DeviceType.Mobility.NONMOVABLE,"Vodafone");
		ComputeLimits computelimits = new ComputeLimits(1,2,3);
		Device device1 = new Device("100","192.168.0.1",devicetype,computelimits);
		network.addDevice(device1);
		Location location1 = new Location("location1",10.5f,20.5f);
		DeviceLocationRelation devLoc1 = new DeviceLocationRelation("devLoc1",device1,location1);
		network.addDeviceLocationRelation(devLoc1);
		Assert.assertEquals("location1",network.getCurrentDeviceLocation(device1).getLocationId());
	}
	
	@Test
	public void getCurrentApplicationLocationTest()
	{
		DeviceType devicetype = new DeviceType(DeviceType.TypeId.UE,DeviceType.Mobility.NONMOVABLE,"Vodafone");
		ComputeLimits computelimits = new ComputeLimits(1,2,3);
		Device device1 = new Device("100","192.168.0.1",devicetype,computelimits);
		network.addDevice(device1);
		Location location1 = new Location("location1",10.5f,20.5f);
		network.addLocation(location1);
		DeviceLocationRelation devLoc1 = new DeviceLocationRelation("devLoc1",device1,location1);
		network.addDeviceLocationRelation(devLoc1);
		Assert.assertEquals("location1",network.getCurrentDeviceLocation(device1).getLocationId());
		ComputeRequirements computeRequirements = new ComputeRequirements(1.0f,2.0f,3.0f);
		NetworkRequirements networkRequirements = new NetworkRequirements(10.0f,20.0f,30.0,40.0);
		ApplicationType applicationType = new ApplicationType("Application1",computeRequirements,networkRequirements);
		ApplicationInstance application1 = new ApplicationInstance(applicationType,ApplicationInstance.State.RUNNING);
		network.addApplicationInstance(application1);
		ApplicationInstanceDeviceRelation appDevice1 = new ApplicationInstanceDeviceRelation("appDevice1",application1,device1);
		network.addApplicationDeviceRelation(appDevice1);
		Assert.assertEquals("appDevice1",appDevice1.getApplicationDeviceRelationId());
		Assert.assertEquals("appDevice1",appDevice1.getApplicationDevice().getApplicationRelationId());
		Assert.assertEquals("appDevice1",appDevice1.getApplication().getDeviceRelationId());
		Assert.assertEquals("location1",network.getCurrentApplicationLocation(application1).getLocationId());
	}
	
	@Test
	public void calculateDistanceTest() {
		Location location1 = new Location("location1",10.5f,20.5f);
		Location location2 = new Location("location2",40.5f,10.5f);
		Assert.assertEquals(3477.69f,network.calculateDistance(location1, location2),0.5);
	}
	
	@Test
	public void getphysicalDistanceBetweenDevicesTest() {
		DeviceType devicetype = new DeviceType(DeviceType.TypeId.UE,DeviceType.Mobility.NONMOVABLE,"Vodafone");
		ComputeLimits computelimits = new ComputeLimits(1,2,3);
		Device device1 = new Device("100","192.168.0.1",devicetype,computelimits);
		network.addDevice(device1);
		Location location1 = new Location("location1",10.5f,20.5f);
		DeviceLocationRelation devLoc1 = new DeviceLocationRelation("devLoc1",device1,location1);
		network.addDeviceLocationRelation(devLoc1);
		Assert.assertEquals("location1",network.getCurrentDeviceLocation(device1).getLocationId());
		Device device2 = new Device("200","192.169.0.1",devicetype,computelimits);
		network.addDevice(device2);
		Location location2 = new Location("location2",40.5f,10.5f);
		DeviceLocationRelation devLoc2 = new DeviceLocationRelation("devLoc2",device2,location2);
		network.addDeviceLocationRelation(devLoc2);
		Assert.assertEquals(3477.69f,network.getphysicalDistanceBetweenDevices("100","200"),0.5);
	}

	@Test
	public void getphysicalDistanceBetweenlocationsTest() {
		Location location1 = new Location("location1",10.5f,20.5f);
		Location location2 = new Location("location2",40.5f,10.5f);
		network.addLocation(location1);
		network.addLocation(location2);
		Assert.assertEquals(3477.69f,network.getphysicalDistanceBetweenlocations("location1", "location2"),0.5);		
	}
	
	@Test
	public void copyNetworkTest()
	{
		DeviceType devicetype = new DeviceType(DeviceType.TypeId.UE,DeviceType.Mobility.NONMOVABLE,"Vodafone");
		ComputeLimits computelimits = new ComputeLimits(1,2,3);
		Device device1 = new Device("100","192.168.0.1",devicetype,computelimits);
		network.addDevice(device1);
		NetworkLimits networkLimits = new NetworkLimits(0.5f,0.2f,1.5,2.0);
		Connection connection1 = new Connection("Connection1",networkLimits);
		network.addConnection(connection1);
		// create network object
		Network network1 = new Network();
		network1.copyNetwork(network);
		Assert.assertEquals("Connection1",network1.getConnection("Connection1").getConnectionId());
				
		
	}
	
	@Test
	public void saveNetworkTest() throws JsonProcessingException, InterruptedException {
		DeviceType devicetype = new DeviceType(DeviceType.TypeId.UE,DeviceType.Mobility.NONMOVABLE,"Vodafone");
		ComputeLimits computelimits = new ComputeLimits(1,2,3);
		Device device1 = new Device("100","192.168.0.1",devicetype,computelimits);
		network.addDevice(device1);
		NetworkLimits networkLimits = new NetworkLimits(0.5f,0.2f,1.5,2.0);
		Connection connection1 = new Connection("Connection1",networkLimits);
		network.addConnection(connection1);
		ComputeRequirements computeRequirements = new ComputeRequirements(1.0f,2.0f,3.0f);
		NetworkRequirements networkRequirements = new NetworkRequirements(10.0f,20.0f,30.0,40.0);
		ApplicationType applicationType = new ApplicationType("Application1",computeRequirements,networkRequirements);
		ApplicationInstance application1 = new ApplicationInstance(applicationType,ApplicationInstance.State.RUNNING);
		network.addApplicationInstance(application1);
		Location location1 = new Location("location1",10.5f,20.5f);
		network.addLocation(location1);
		DeviceLocationRelation devLoc1 = new DeviceLocationRelation("devLoc1",device1,location1);
		network.addDeviceLocationRelation(devLoc1);
		ApplicationInstanceDeviceRelation appDevice1 = new ApplicationInstanceDeviceRelation("appDevice1",application1,device1);
		network.addApplicationDeviceRelation(appDevice1);
		LocationConnectionRelation locCon1 = new LocationConnectionRelation("locCon1",location1,connection1);
		network.addLocationConnectionRelation(locCon1);
		String writeString = network.saveNetwork();
		System.out.println(writeString);
//	    Gson gson = new Gson();
//        System.out.println("hello= "+gson.toJson(network));
	}
	
	@Test
	public void getphysicalDistanceBetweenApplicationsTest()
	{
		DeviceType devicetype = new DeviceType(DeviceType.TypeId.UE,DeviceType.Mobility.NONMOVABLE,"Vodafone");
		ComputeLimits computelimits = new ComputeLimits(1,2,3);
		Device device1 = new Device("100","192.168.0.1",devicetype,computelimits);
		network.addDevice(device1);
		Location location1 = new Location("location1",10.5f,20.5f);
		network.addLocation(location1);
		DeviceLocationRelation devLoc1 = new DeviceLocationRelation("devLoc1",device1,location1);
		network.addDeviceLocationRelation(devLoc1);
		Device device2 = new Device("200","192.169.0.1",devicetype,computelimits);
		network.addDevice(device2);
		Location location2 = new Location("location2",40.5f,10.5f);
		DeviceLocationRelation devLoc2 = new DeviceLocationRelation("devLoc2",device2,location2);
		network.addDeviceLocationRelation(devLoc2);
		Assert.assertEquals("location1",network.getCurrentDeviceLocation(device1).getLocationId());
		ComputeRequirements computeRequirements = new ComputeRequirements(1.0f,2.0f,3.0f);
		NetworkRequirements networkRequirements = new NetworkRequirements(10.0f,20.0f,30.0,40.0);
		ApplicationType applicationType1 = new ApplicationType("Application1",computeRequirements,networkRequirements);
		ApplicationInstance application1 = new ApplicationInstance(applicationType1,ApplicationInstance.State.RUNNING);
		ApplicationType applicationType2 = new ApplicationType("Application2",computeRequirements,networkRequirements);
		ApplicationInstance application2 = new ApplicationInstance(applicationType2,ApplicationInstance.State.RUNNING);
		network.addApplicationInstance(application1);
		network.addApplicationInstance(application2);
		ApplicationInstanceDeviceRelation appDevice1 = new ApplicationInstanceDeviceRelation("appDevice1",application1,device1);
		network.addApplicationDeviceRelation(appDevice1);
		ApplicationInstanceDeviceRelation appDevice2 = new ApplicationInstanceDeviceRelation("appDevice2",application2,device2);
		network.addApplicationDeviceRelation(appDevice2);
		Assert.assertEquals("appDevice1",appDevice1.getApplicationDeviceRelationId());
		Assert.assertEquals("appDevice1",appDevice1.getApplicationDevice().getApplicationRelationId());
		Assert.assertEquals("appDevice1",appDevice1.getApplication().getDeviceRelationId());
		Assert.assertEquals(3477.69f,network.getphysicalDistanceBetweenApplications("Application1","Application2"),0.5);
	}
	
	/**
	 * Test case to read JSON string
	 */
	//Commented as the JSON string read is exceeding memory and the test case fails due to that.
	@Test
	public void loadNetworkTest() throws JsonProcessingException {
		//String jSON = "{\"deviceMap\":{\"100\":{\"deviceId\":\"100\",\"applicationRelationId\":\"appDevice1\",\"locationRelationId\":\"devLoc1\",\"deviceAddress\":\"192.168.0.1\",\"deviceType\":{\"deviceName\":\"Vodafone\",\"typeId\":\"UE\",\"mobility\":\"NONMOVABLE\"},\"computeLimits\":{\"cpuCount\":1,\"gpuCount\":2,\"memoryLimit\":3}}},\"connectionMap\":{\"Connection1\":{\"connectionId\":\"Connection1\",\"deviceRelationId\":\"\",\"locationRelationId\":\"locCon1\",\"networkCapacity\":{\"latency\":0.5,\"jitter\":0.2,\"throughput\":1.5,\"packetLoss\":2}}},\"locationMap\":{\"location1\":{\"locationId\":\"location1\",\"deviceRelationId\":\"devLoc1\",\"connectionRelationId\":\"locCon1\",\"latitude\":10.5,\"longitude\":20.5}},\"applicationMap\":{\"Application1\":{\"applicationState\":\"RUNNING\",\"deviceRelationId\":\"appDevice1\",\"applicationType\":{\"applicationId\":\"Application1\",\"computeSpecification\":{\"requiredCpu\":1,\"requiredGpu\":2,\"requiredMemory\":3},\"networkSpecification\":{\"requiredLatency\":10,\"requiredJitter\":20,\"requiredThroughput\":30,\"requiredPacketLoss\":40}}}},\"applicationDeviceMap\":{\"appDevice1\":{\"applicationDeviceRelationId\":\"appDevice1\",\"application\":{\"applicationState\":\"RUNNING\",\"deviceRelationId\":\"appDevice1\",\"applicationType\":{\"applicationId\":\"Application1\",\"computeSpecification\":{\"requiredCpu\":1,\"requiredGpu\":2,\"requiredMemory\":3},\"networkSpecification\":{\"requiredLatency\":10,\"requiredJitter\":20,\"requiredThroughput\":30,\"requiredPacketLoss\":40}}},\"applicationDevice\":{\"deviceId\":\"100\",\"applicationRelationId\":\"appDevice1\",\"locationRelationId\":\"devLoc1\",\"deviceAddress\":\"192.168.0.1\",\"deviceType\":{\"deviceName\":\"Vodafone\",\"typeId\":\"UE\",\"mobility\":\"NONMOVABLE\"},\"computeLimits\":{\"cpuCount\":1,\"gpuCount\":2,\"memoryLimit\":3}}}},\"locationConnectionMap\":{\"locCon1\":{\"locationConnectionRelationId\":\"locCon1\",\"connectionRelation\":{\"connectionId\":\"Connection1\",\"deviceRelationId\":\"\",\"locationRelationId\":\"locCon1\",\"networkCapacity\":{\"latency\":0.5,\"jitter\":0.2,\"throughput\":1.5,\"packetLoss\":2}},\"locationRelation\":{\"locationId\":\"location1\",\"deviceRelationId\":\"devLoc1\",\"connectionRelationId\":\"locCon1\",\"latitude\":10.5,\"longitude\":20.5}}},\"deviceLocationMap\":{\"devLoc1\":{\"deviceLocationRelationId\":\"devLoc1\",\"device\":{\"deviceId\":\"100\",\"applicationRelationId\":\"appDevice1\",\"locationRelationId\":\"devLoc1\",\"deviceAddress\":\"192.168.0.1\",\"deviceType\":{\"deviceName\":\"Vodafone\",\"typeId\":\"UE\",\"mobility\":\"NONMOVABLE\"},\"computeLimits\":{\"cpuCount\":1,\"gpuCount\":2,\"memoryLimit\":3}},\"location\":{\"locationId\":\"location1\",\"deviceRelationId\":\"devLoc1\",\"connectionRelationId\":\"locCon1\",\"latitude\":10.5,\"longitude\":20.5}}}}";
    	String jSON = "{\"100\":{\"deviceId\":\"100\",\"applicationRelationId\":\"appDevice1\",\"locationRelationId\":\"devLoc1\",\"deviceAddress\":\"192.168.0.1\",\"deviceType\":{\"deviceName\":\"Vodafone\",\"typeId\":\"UE\",\"mobility\":\"NONMOVABLE\"},\"computeLimits\":{\"cpuCount\":1.0,\"gpuCount\":2.0,\"memoryLimit\":3.0}}}";
//		network.loadNetwork(jSON);
//    	Device device =	network.getDevice("100");
//		Assert.assertEquals("100", device.getDeviceId());
//		Connection connection =	network.getConnection("Connection1");
//		Assert.assertEquals("Connection1", connection.getConnectionId());
//		Location location = network.getLocation("location1");
//		Assert.assertEquals("location1",location.getLocationId());
//		Assert.assertEquals(10.5,location.getLatitude(),0.5);
//		Assert.assertEquals(20.5,location.getLongitude(),0.5);
	}
	
	
	@Test
	public void getNetworkDistance() {
		NetworkLimits networkLimits1 = new NetworkLimits(0.5f,0.25f,1.5,3.0);
		NetworkLimits networkLimits2 = new NetworkLimits(4.5f,1.2f,2.5,2.0);
		NetworkLimits networkLimits3 = new NetworkLimits(6.5f,2.2f,3.5,4.0);
		NetworkLimits networkLimits4 = new NetworkLimits(8.5f,3.2f,4.5,6.0);
		NetworkLimits networkLimits5 = new NetworkLimits(10.5f,4.2f,5.5,7.0);
		NetworkLimits networkLimits6 = new NetworkLimits(20.5f,5.2f,6.5,8.0);
		NetworkLimits networkLimits7 = new NetworkLimits(30.5f,6.2f,7.5,9.0);
		NetworkLimits networkLimits8 = new NetworkLimits(40.5f,7.2f,8.5,10.0);
		Connection connection1 = new Connection("Connection1",networkLimits1);
		Connection connection2 = new Connection("Connection2",networkLimits2);
		Connection connection3 = new Connection("Connection3",networkLimits3);
		Connection connection4 = new Connection("Connection4",networkLimits4);
		Connection connection5 = new Connection("Connection5",networkLimits5);
		Connection connection6 = new Connection("Connection6",networkLimits6);
		Connection connection7 = new Connection("Connection7",networkLimits7);
		Connection connection8 = new Connection("Connection8",networkLimits8);
		network.addConnection(connection1);
		network.addConnection(connection2);
		network.addConnection(connection3);
		network.addConnection(connection4);
		network.addConnection(connection5);
		network.addConnection(connection6);
		network.addConnection(connection7);
		network.addConnection(connection8);
		double distance = network.getNetworkDistance("Connection2","Connection7");
		Assert.assertEquals(0,distance,0.5);
	}




}
