package InfrastructureManager.NetworkStructure;
import java.util.ArrayList;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;

/**
 * This class contains network information.
 *
 * @author Shankar Lokeshwara
 */

public class Network {
	enum Update {ADD,DELETE};
	private final ObjectMapper mapper;
	private List<Device> deviceList;
	private List<Connection> connectionList;
	private List<Location> locationList;
	private List<ApplicationInstance> applicationList;
	private List<ApplicationInstanceDeviceRelation> applicationDeviceList;
	private List<LocationConnectionRelation> locationConnectionList;
	private List<DeviceLocationRelation> deviceLocationList;

	/**
	 * Default constructor for Network Class
	 */
	public Network() {
		this.mapper = new ObjectMapper();
		deviceList = new ArrayList<>();
		connectionList = new ArrayList<>();
		locationList = new ArrayList<>();
		applicationList = new ArrayList<>();
		applicationDeviceList = new ArrayList<>();
		locationConnectionList= new ArrayList<>();
		deviceLocationList = new ArrayList<>();
	}

	/**
	 * Function to read JSON string and update network class
	 * @param String
	 */
	public void loadNetwork(String readString) throws JsonProcessingException{

		//New object created as the called object cannot be accessed as a whole. Therefore new object is created to update the calling object members with values from json string
		Network clonedObject = this.mapper.readValue(readString,Network.class);
		copyNetwork(clonedObject
				);
	}

	/**
	 * Function to update network class
	 * @param Network object
	 */
	public void copyNetwork(Network network) {
		this.deviceList = network.deviceList;
		this.connectionList = network.connectionList;
		this.locationList = network.locationList;
		this.applicationList = network.applicationList;
		this.applicationDeviceList = network.applicationDeviceList;
		this.locationConnectionList = network.locationConnectionList;
		this.deviceLocationList = network.deviceLocationList;
	}


	/**
	 * Function to save JSON string
	 * @return String
	 */
	public String saveNetwork() throws JsonProcessingException{
		String json = this.mapper.writeValueAsString(this);
		return json;
	}


	/**
	 * Getter function to get a particular device from deviceList
	 * @param deviceId
	 * @return Device
	 */
	public Device getDeviceFromList(String deviceId)	{
		int idx=0;
		for (Object obj : this.deviceList) {
			Device device = (Device)obj;	
			if (device.getDeviceId() == deviceId)
			{
				idx = this.deviceList.indexOf(obj);
			}
		}
		return this.deviceList.get(idx);
	}

	/**
	 * Function to update a particular device in deviceList
	 * @param Device
	 * @param UpdateType // ADD or DELETE
	 */
	public void updateDeviceList(Device device,Network.Update update){
		if (update == Network.Update.ADD)
		{
			this.deviceList.add(device);
		}
		else if(update == Network.Update.DELETE) {
				for (Object obj : this.deviceList) {
					Device device1 = (Device)obj;
					if (device1.getDeviceId() == device.getDeviceId())
					{
						this.deviceList.remove(this.deviceList.indexOf(obj));
					}
				} 
		}
	}



	/**
	 * Getter function to get a particular connection from connectionList
	 * @param ConnectionId
	 * @return Connection
	 */
	public Connection getConnectionFromList(String ConnectionId)	{
		int idx=0;
		for (Object obj : this.connectionList) {
			Connection connection = (Connection)obj;
			if (connection.getConnectionId() == ConnectionId)
			{
				idx = this.connectionList.indexOf(obj);
			}
		}
		return this.connectionList.get(idx);
	}

	/**
	 * Function to update a particular connection in connectionList
	 * @param Connection
	 * @param UpdateType // ADD or DELETE
	 */
	public void updateConnectionList(Connection connection,Network.Update update){
		if (update == Network.Update.ADD)
		{
			this.connectionList.add(connection);
		}
		else if(update == Network.Update.DELETE) {
			for (Object obj : this.connectionList) {
				Connection connection1 = (Connection)obj;
				if (connection1.getConnectionId() == connection.getConnectionId())
				{
					this.connectionList.remove(this.connectionList.indexOf(obj));
				}
			} 
		}
	}

	/**
	 * Getter function to get a particular location from locationList
	 * @param LocationId
	 * @return Location
	 */
	public Location getLocationFromList(String LocationId)	{
		int idx=0;
		for (Object obj : this.locationList) {
			Location location = (Location)obj;
			if (location.getLocationId() == LocationId)
			{
				idx = this.locationList.indexOf(obj);
			}
		}
		return this.locationList.get(idx);
	}

	/**
	 * Function to update a particular location in locationList
	 * @param Location
	 * @param UpdateType // ADD or DELETE
	 */
	public void updateLocationList(Location location,Network.Update update){
		if (update == Network.Update.ADD)
		{
			this.locationList.add(location);
		}
		else if(update == Network.Update.DELETE) {
			for (Object obj : this.locationList) {
				Location location1 = (Location)obj;
				if (location1.getLocationId() == location.getLocationId())
				{
					this.locationList.remove(this.locationList.indexOf(obj));
				}
			} 
		}
	}
	
	
	/**
	 * Getter function to get a particular application from applicationList
	 * @param applicationId
	 * @return ApplicationInstance
	 */
	public ApplicationInstance getApplicationInstanceFromList(String ApplicationId)	{
		int idx=0;
		for (Object obj : this.applicationList) {
			ApplicationInstance application = (ApplicationInstance)obj;
			if (application.getApplicationType().getApplicationId() == ApplicationId)
			{
				idx = this.applicationList.indexOf(obj);
			}
		}
		return this.applicationList.get(idx);
	}

	/**
	 * Function to update a particular application in applicationList
	 * @param application
	 * @param UpdateType // ADD or DELETE
	 */
	public void updateApplicationInstanceList(ApplicationInstance application,Network.Update update){
		if (update == Network.Update.ADD)
		{
			this.applicationList.add(application);
		}
		else if(update == Network.Update.DELETE) {
			for (Object obj : this.applicationList) {
				ApplicationInstance application1 = (ApplicationInstance)obj;
				if (application1.getApplicationType().getApplicationId() == application.getApplicationType().getApplicationId())
				{
					this.applicationList.remove(this.applicationList.indexOf(obj));
				}
			} 
		}
	}

	/**
	 * Getter function
	 * @return applicationDeviceList
	 */
	public List<ApplicationInstanceDeviceRelation> getApplicationDeviceList() {
		return applicationDeviceList;
	}

	/**
	 * Getter function
	 * @return locationConnectionList
	 */
	public List<LocationConnectionRelation> getLocationConnectionList() {
		return locationConnectionList;
	}

	/**
	 * Getter function
	 * @return deviceLocationList
	 */
	public List<DeviceLocationRelation> getDeviceLocationList() {
		return deviceLocationList;
	}

	/**
	 * Setter function
	 * @param applicationDeviceList
	 */
	public void setApplicationDeviceList(List<ApplicationInstanceDeviceRelation> applicationDeviceList) {
		this.applicationDeviceList = applicationDeviceList;
	}

	/**
	 * Setter function
	 * @param locationConnectionList
	 */
	public void setLocationConnectionList(List<LocationConnectionRelation> locationConnectionList) {
		this.locationConnectionList = locationConnectionList;
	}

	/**
	 * Setter function
	 * @param deviceLocationList
	 */
	public void setDeviceLocationList(List<DeviceLocationRelation> deviceLocationList) {
		this.deviceLocationList = deviceLocationList;
	}
}
