package InfrastructureManager.NetworkStructure;
import java.util.ArrayList;
import java.util.Arrays;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;
public class Network {
	
	private List<Device> deviceList;
	private final ObjectMapper mapper;
	private List<Connection> connectionList;
    private List<Location> locationList;
    private List<ApplicationInstance> applicationList;
	
	
	public Network() {
		this.mapper = new ObjectMapper();
		deviceList = new ArrayList<>();
		connectionList = new ArrayList<>();
		locationList = new ArrayList<>();
		applicationList = new ArrayList<>();
	}

	public void loadNetwork(String readString) throws JsonProcessingException{
		JsonNode newNetwork = this.mapper.readTree(readString);
		System.out.println(newNetwork);
		Network obj = this.mapper.readValue(readString,Network.class);
		System.out.println(obj.deviceList);
		System.out.println(obj.connectionList);
		
	}
	
	//ToDO - implement the JSON write
	public String saveNetwork() throws JsonProcessingException{
		String json = null;
		//ObjectNode resultObject = mapper.createObjectNode();
		try {
		    // create book object
			List<Device> deviceList = Arrays.asList(new Device(001),new Device(002),new Device(003),new Device(004));
			List<Connection> connectionList = Arrays.asList(new Connection(100),new Connection(101),new Connection(102),new Connection(103),new Connection(104));
			List <Location> locationList = Arrays.asList(new Location(500),new Location(501),new Location(502),new Location(503),new Location(504));
			List<ApplicationInstance> applicationList = Arrays.asList(new ApplicationInstance(600),new ApplicationInstance(601),new ApplicationInstance(602),new ApplicationInstance(603),new ApplicationInstance(604));
			// convert book object to JSON
			Network network = new Network();
			network.applicationList = applicationList;
			network.connectionList = connectionList;
			network.deviceList = deviceList;
			network.locationList=locationList;
			json = this.mapper.writeValueAsString(network);

		} catch (Exception ex) {
		    ex.printStackTrace();
		}
		return json;
		
	}
	
    public List<ApplicationInstance> getApplicationList() {
        return applicationList;
    }

    public List<Connection> getConnectionList() {
        return connectionList;
    }

    public List<Device> getDeviceList() {
        return deviceList;
    }

    public List<Location> getLocationList() {
        return locationList;
    }
	
	public static void main(String[] args) throws JsonProcessingException
	{
		Network network = new Network();
		String writeString = network.saveNetwork();
		network.loadNetwork(writeString);
	}	
}
