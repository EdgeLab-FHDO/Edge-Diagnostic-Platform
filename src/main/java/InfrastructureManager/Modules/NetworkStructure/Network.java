package InfrastructureManager.Modules.NetworkStructure;
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
	public enum Update {ADD,DELETE};
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
	public String saveNetwork(){
		try {
			String json = this.mapper.writeValueAsString(this);
			return json;
		}
		catch(JsonProcessingException e) {
			return null;
		}

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
			if (device.getDeviceId().equals(deviceId))
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
				if (device1.getDeviceId().equals(device.getDeviceId()))
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
			if (connection.getConnectionId().equals(ConnectionId))
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
				if (connection1.getConnectionId().equals(connection.getConnectionId()))
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
			if (location.getLocationId().equals(LocationId))
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
				if (location1.getLocationId().equals(location.getLocationId()))
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
			if (application.getApplicationType().getApplicationId().equals( ApplicationId))
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
				if (application1.getApplicationType().getApplicationId().equals(application.getApplicationType().getApplicationId()))
				{
					this.applicationList.remove(this.applicationList.indexOf(obj));
				}
			} 
		}
	}

	/**
	 * Getter function to get a particular ApplicationInstanceDeviceRelation from applicationDeviceList
	 * @param id
	 * @return ApplicationInstanceDeviceRelation
	 */
	public ApplicationInstanceDeviceRelation getApplicationInstanceDeviceRelationFromList(String id)	{
		int idx=0;
		for (Object obj : this.applicationDeviceList) {
			ApplicationInstanceDeviceRelation applicationDevice = (ApplicationInstanceDeviceRelation)obj;
			if ((applicationDevice.getApplication().getApplicationType().getApplicationId().equals(id)) || applicationDevice.getApplicationDevice().getDeviceId().equals(id))
			{
				idx = this.applicationDeviceList.indexOf(obj);
			}
		}
		return this.applicationDeviceList.get(idx);
	}

	/**
	 * Function to update a particular applicationDevice in applicationDeviceList
	 * @param applicationDevice
	 * @param UpdateType // ADD or DELETE
	 */
	public void updateapplicationDeviceList(ApplicationInstanceDeviceRelation applicationDevice,Network.Update update){
		if (update == Network.Update.ADD)
		{
			this.applicationDeviceList.add(applicationDevice);
		}
		else if(update == Network.Update.DELETE) {
			for (Object obj : this.applicationDeviceList) {
				ApplicationInstanceDeviceRelation applicationDevice1 = (ApplicationInstanceDeviceRelation)obj;
				if (applicationDevice1.getApplication().getApplicationType().getApplicationId().equals(applicationDevice.getApplication().getApplicationType().getApplicationId()) ||
						applicationDevice1.getApplicationDevice().getDeviceId().equals(applicationDevice.getApplicationDevice().getDeviceId()))
				{
					this.applicationDeviceList.remove(this.applicationDeviceList.indexOf(obj));
				}
			} 
		}
	}

	/**
	 * Getter function to get a particular LocationConnectionRelation from locationConnectionList
	 * @param id
	 * @return LocationConnectionRelation
	 */
	public LocationConnectionRelation getLocationConnectionRelationFromList(String id)	{
		int idx=0;
		for (Object obj : this.locationConnectionList) {
			LocationConnectionRelation locationConnection = (LocationConnectionRelation)obj;
			if ((locationConnection.getConnectionRelation().getConnectionId().equals(id)) || locationConnection.getLocationRelation().getLocationId().equals(id))
			{
				idx = this.locationConnectionList.indexOf(obj);
			}
		}
		return this.locationConnectionList.get(idx);
	}

	/**
	 * Function to update a particular locationConnection in locationConnectionList
	 * @param locationConnection
	 * @param UpdateType // ADD or DELETE
	 */
	public void updatelocationConnectionList(LocationConnectionRelation locationConnection,Network.Update update){
		if (update == Network.Update.ADD)
		{
			this.locationConnectionList.add(locationConnection);
		}
		else if(update == Network.Update.DELETE) {
			for (Object obj : this.locationConnectionList) {
				LocationConnectionRelation locationConnection1 = (LocationConnectionRelation)obj;
				if (locationConnection1.getConnectionRelation().getConnectionId().equals(locationConnection.getConnectionRelation().getConnectionId()) ||
						locationConnection1.getLocationRelation().getLocationId().equals(locationConnection.getLocationRelation().getLocationId()))
				{
					this.locationConnectionList.remove(this.locationConnectionList.indexOf(obj));
				}
			} 
		}
	}


	/**
	 * Getter function to get a particular DeviceLocationRelation from deviceLocationList
	 * @param id
	 * @return DeviceLocationRelation
	 */
	public DeviceLocationRelation getDeviceLocationRelationFromList(String id)	{
		DeviceLocationRelation deviceLocation = null;
		for (Object obj : this.deviceLocationList) {
			DeviceLocationRelation deviceLocationObj = (DeviceLocationRelation)obj;
			if ((deviceLocationObj.getDevice().getDeviceId().equals(id)) || deviceLocationObj.getLocation().getLocationId().equals(id) )
			{
				deviceLocation = this.deviceLocationList.get(this.deviceLocationList.indexOf(obj));
			}
		}
		return (deviceLocation);
	}

	/**
	 * Function to update a particular deviceLocation in deviceLocationList
	 * @param deviceLocation
	 * @param UpdateType // ADD or DELETE
	 */
	public void updateDeviceLocationList(DeviceLocationRelation deviceLocation,Network.Update update){
		if (update == Network.Update.ADD)
		{
			this.deviceLocationList.add(deviceLocation);
		}
		else if(update == Network.Update.DELETE) {
			for (Object obj : this.deviceLocationList) {
				DeviceLocationRelation deviceLocation1 = (DeviceLocationRelation)obj;
				if (deviceLocation1.getDevice().getDeviceId().equals(deviceLocation.getDevice().getDeviceId()) ||
						deviceLocation1.getLocation().getLocationId().equals(deviceLocation.getLocation().getLocationId()))
				{
					this.deviceLocationList.remove(this.deviceLocationList.indexOf(obj));
				}
			} 
		}
	}
	/**
	 * Function to get location of a particular device instance
	 * @param deviceId
	 */
	public Location getCurrentDeviceLocation(String id) {
		Location location = null;
		if(id.equals(this.getDeviceLocationRelationFromList(id).getDevice().getDeviceId())) {
			location = this.getDeviceLocationRelationFromList(id).getLocation();  		
		}
		return location;
	}

	/**
	 * Function to get location of a particular application instance
	 * @param applicationId
	 */
	public Location getCurrentApplicationLocation(String id) {
		Location location = null;
		if (id.equals(this.getApplicationInstanceDeviceRelationFromList(id).getApplication().getApplicationType().getApplicationId())) {
			location = this.getDeviceLocationRelationFromList(this.getApplicationInstanceDeviceRelationFromList(id).getApplicationDevice().getDeviceId()).getLocation();
		}
		return location;
	}

	/**
	 * Function to calculate distance between two locations.
	 * @param sourceLocation and destinationLocation
	 * @return distance
	 */
	public double calculateDistance(Location source,Location destination)
	{
		double earthRadius = 6371.01; //Kilometers
		double distance = 0;
		if ((source.getLatitude() == destination.getLatitude()) && (source.getLongitude() == destination.getLongitude())) {
			distance =  0;
		}

		else {
			double theta = source.getLongitude() - destination.getLongitude();
			double sourceLatitudeRadians = Math.toRadians(source.getLatitude());
			double destinationLatitudeRadians = Math.toRadians(destination.getLatitude());
			distance = Math.acos(Math.sin(sourceLatitudeRadians)*Math.sin(destinationLatitudeRadians) + Math.cos(sourceLatitudeRadians)*Math.cos(destinationLatitudeRadians)*Math.cos(Math.toRadians(theta)));
			distance = earthRadius * distance;     
		}
		return distance;
	}

	/**
	 * Function to get physical distance between devices
	 * @param sourceId and destinationId
	 */
	public double getphysicalDistanceBetweenDevices(String sourceId, String destinationId) {
		Location sourceLocation,destinationLocation;
		sourceLocation = getCurrentDeviceLocation(sourceId);
		destinationLocation = getCurrentDeviceLocation(destinationId);
		double distance = this.calculateDistance(sourceLocation,destinationLocation);
		return distance;
	}

	/**
	 * Function to get physical distance between application instances.
	 * @param sourceId and destinationId
	 */
	public double getphysicalDistanceBetweenApplications(String sourceId, String destinationId) {
		Location sourceLocation,destinationLocation;
		sourceLocation = getCurrentApplicationLocation(sourceId);
		destinationLocation = getCurrentApplicationLocation(destinationId);
		double distance = this.calculateDistance(sourceLocation,destinationLocation);
		return distance;
	}

	/**
	 * Function to get network distance between devices or between application instances.
	 * @param sourceId and destinationId
	 */
	public double getNetworkDistance(String sourceId, String destinationId) {
		Connection source,destination;
		source = this.getConnectionFromList(sourceId);
		destination = this.getConnectionFromList(destinationId);
		double distance = source.getNetworkCapacity().getLatency() + destination.getNetworkCapacity().getLatency() ; // Write a function to get the network path. To write a search algorithm //One path between source and destination
		return distance;
	}  

}
