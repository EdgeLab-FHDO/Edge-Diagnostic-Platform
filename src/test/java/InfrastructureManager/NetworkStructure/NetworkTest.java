package InfrastructureManager.NetworkStructure;
import org.junit.Assert;
import org.junit.Test;
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
	@Test
	public void saveNetworkTest() throws JsonProcessingException {
	 // create network object
    	Network network = new Network();
    	network.setDeviceList(Arrays.asList(new Device(001),new Device(002),new Device(003),new Device(004)));
    	network.setConnectionList(Arrays.asList(new Connection(100),new Connection(101),new Connection(102),new Connection(103),new Connection(104)));
    	network.setLocationList(Arrays.asList(new Location(500),new Location(501),new Location(502),new Location(503),new Location(504)));
    	network.setApplicationList(Arrays.asList(new ApplicationInstance(600),new ApplicationInstance(601),new ApplicationInstance(602),new ApplicationInstance(603),new ApplicationInstance(604)));
		String writeString = network.saveNetwork();
		System.out.println(writeString);
		int deviceID0 = network.getDeviceList().get(0).getDeviceId();
		System.out.println(deviceID0);
	    Assert.assertEquals(1, deviceID0);
	}
	
	/**
	 * Test case to read JSON string
	 */
	@Test
	public void loadNetworkTest() throws JsonProcessingException {
		String jSON = "{\"deviceList\":[{\"deviceId\":1,\"deviceAddress\":\"\",\"deviceType\":{\"deviceName\":\"\",\"typeId\":\"NONE\",\"mobility\":\"NONE\"},\"computeLimits\":{\"cpuCount\":0,\"gpuCount\":0,\"memoryLimit\":0}},{\"deviceId\":2,\"deviceAddress\":\"\",\"deviceType\":{\"deviceName\":\"\",\"typeId\":\"NONE\",\"mobility\":\"NONE\"},\"computeLimits\":{\"cpuCount\":0,\"gpuCount\":0,\"memoryLimit\":0}},{\"deviceId\":3,\"deviceAddress\":\"\",\"deviceType\":{\"deviceName\":\"\",\"typeId\":\"NONE\",\"mobility\":\"NONE\"},\"computeLimits\":{\"cpuCount\":0,\"gpuCount\":0,\"memoryLimit\":0}},{\"deviceId\":4,\"deviceAddress\":\"\",\"deviceType\":{\"deviceName\":\"\",\"typeId\":\"NONE\",\"mobility\":\"NONE\"},\"computeLimits\":{\"cpuCount\":0,\"gpuCount\":0,\"memoryLimit\":0}}],\"connectionList\":[{\"connectionId\":100,\"networkCapacity\":{\"latency\":0,\"throughput\":0,\"packetLoss\":0}},{\"connectionId\":101,\"networkCapacity\":{\"latency\":0,\"throughput\":0,\"packetLoss\":0}},{\"connectionId\":102,\"networkCapacity\":{\"latency\":0,\"throughput\":0,\"packetLoss\":0}},{\"connectionId\":103,\"networkCapacity\":{\"latency\":0,\"throughput\":0,\"packetLoss\":0}},{\"connectionId\":104,\"networkCapacity\":{\"latency\":0,\"throughput\":0,\"packetLoss\":0}}],\"locationList\":[{\"locationId\":500,\"latitude\":0,\"longitude\":0},{\"locationId\":501,\"latitude\":0,\"longitude\":0},{\"locationId\":502,\"latitude\":0,\"longitude\":0},{\"locationId\":503,\"latitude\":0,\"longitude\":0},{\"locationId\":504,\"latitude\":0,\"longitude\":0}],\"applicationList\":[{\"applicationState\":\"IDLE\",\"applicationInstance\":{\"applicationId\":600,\"computeSpecification\":{\"requiredCpu\":0,\"requiredGpu\":0,\"requiredMemory\":0},\"networkSpecification\":{\"requiredLatency\":0,\"requiredThroughput\":0,\"requiredPacketLoss\":0}}},{\"applicationState\":\"IDLE\",\"applicationInstance\":{\"applicationId\":601,\"computeSpecification\":{\"requiredCpu\":0,\"requiredGpu\":0,\"requiredMemory\":0},\"networkSpecification\":{\"requiredLatency\":0,\"requiredThroughput\":0,\"requiredPacketLoss\":0}}},{\"applicationState\":\"IDLE\",\"applicationInstance\":{\"applicationId\":602,\"computeSpecification\":{\"requiredCpu\":0,\"requiredGpu\":0,\"requiredMemory\":0},\"networkSpecification\":{\"requiredLatency\":0,\"requiredThroughput\":0,\"requiredPacketLoss\":0}}},{\"applicationState\":\"IDLE\",\"applicationInstance\":{\"applicationId\":603,\"computeSpecification\":{\"requiredCpu\":0,\"requiredGpu\":0,\"requiredMemory\":0},\"networkSpecification\":{\"requiredLatency\":0,\"requiredThroughput\":0,\"requiredPacketLoss\":0}}},{\"applicationState\":\"IDLE\",\"applicationInstance\":{\"applicationId\":604,\"computeSpecification\":{\"requiredCpu\":0,\"requiredGpu\":0,\"requiredMemory\":0},\"networkSpecification\":{\"requiredLatency\":0,\"requiredThroughput\":0,\"requiredPacketLoss\":0}}}]}";
		 // create network object
		//ObjectMapper mapper = new ObjectMapper();
    	//Network network = mapper.readValue(jSON, Network.class);
		Network network = new Network();
    	network.loadNetwork(jSON);
		int deviceID1 = network.getDeviceList().get(1).getDeviceId();
	    Assert.assertEquals(2, deviceID1);
		System.out.println(deviceID1);
	}
	
	
}
