package InfrastructureManager.Modules.NetworkStructure;
import java.util.HashMap;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;

/**
 * This class contains network information.
 *
 * @author Shankar Lokeshwara
 */

public class Network {
	private final ObjectMapper mapper;
	private Map<String, Device> deviceMap;
	private Map<String, Connection> connectionMap;
	private Map<String, Location> locationMap;
	private Map<String, ApplicationInstance> applicationMap;
	private Map<String, ApplicationInstanceDeviceRelation> applicationDeviceMap;
	private Map<String, LocationConnectionRelation> locationConnectionMap;
	private Map<String, DeviceLocationRelation> deviceLocationMap;

	/**
	 * Default constructor for Network Class
	 */
	public Network() {
		this.mapper = new ObjectMapper();
		deviceMap = new HashMap<>();
		connectionMap = new HashMap<>();
		locationMap = new HashMap<>();
		applicationMap = new HashMap<>();
		applicationDeviceMap = new HashMap<>();
		locationConnectionMap= new HashMap<>();
		deviceLocationMap = new HashMap<>();
	}

	/**
	 * Getter function to get a particular device from deviceMap
	 * @param deviceId
	 * @return Device
	 */
	public Device getDevice(String deviceId)	{
		return this.deviceMap.get(deviceId);
	}

	/**
	 * Function to add a new device to deviceMap
	 * @param Device
	 */
	public void addDevice(Device device){
			this.deviceMap.put(device.getDeviceId(),device);
	}

	/**
	 * Function to delete a particular device from deviceMap
	 * @param deviceId
	 */
	public void removeDevice(String deviceId){
			deviceMap.remove(deviceId);
	}
	


	/**
	 * Getter function to get a particular connection from connectionMap
	 * @param connectionId
	 */
	public Connection getConnection(String connectionId)	{
		return this.connectionMap.get(connectionId);
	}

	/**
	 * Function to add a particular connection to connectionMap
	 * @param Connection
	 */
	public void addConnection(Connection connection){
		this.connectionMap.put(connection.getConnectionId(),connection);
	}
	
	/**
	 * Function to delete a particular connection from connectionMap
	 * @param Connection
	 */
	public void removeConnection(String connectionId){
		this.connectionMap.remove(connectionId);
	}

	/**
	 * Getter function to get a particular location from locationMap
	 * @param locationId
	 * @return Location
	 */
	public Location getLocation(String locationId)	{
		return this.locationMap.get(locationId);
	}

	/**
	 * Function to add a particular location to locationMap
	 * @param Location
	 */
	public void addLocation(Location location){
		this.locationMap.put(location.getLocationId(),location);
	}
	
	/**
	 * Function to delete a particular location from locationMap
	 */
	public void removeLocation(String locationId){
		this.locationMap.remove(locationId);
	}


	/**
	 * Getter function to get a particular application from applicationMap
	 * @param applicationId
	 * @return ApplicationInstance
	 */
	public ApplicationInstance getApplicationInstance(String ApplicationId)	{
		return this.applicationMap.get(ApplicationId);
	}

	/**
	 * Function to add a particular application to applicationMap
	 * @param application
	 */
	public void addApplicationInstance(ApplicationInstance application){
		this.applicationMap.put(application.getApplicationType().getApplicationId(),application);
	}
	
	/**
	 * Function to delete a particular application from applicationMap
	 */
	public void removeApplicationInstance(String ApplicationId){
		this.applicationMap.remove(ApplicationId);
	}

	/**
	 * Getter function to get a particular ApplicationInstanceDeviceRelation from applicationdeviceMap
	 * @param applicationDeviceRelationId
	 * @return ApplicationInstanceDeviceRelation
	 */
	public ApplicationInstanceDeviceRelation getApplicationInstanceDeviceRelation(String applicationDeviceRelationId)	{
		return this.applicationDeviceMap.get(applicationDeviceRelationId);
	}

	/**
	 * Function to add a particular applicationDevice in applicationdeviceMap
	 * @param applicationDevice
	 */
	public void addApplicationDeviceRelation(ApplicationInstanceDeviceRelation applicationDevice){
		this.applicationDeviceMap.put(applicationDevice.getApplicationDeviceRelationId(),applicationDevice);

	}
	
	/**
	 * Function to remove a particular applicationDevice in applicationdeviceMap
	 * @param applicationDeviceRelationId
	 */
	public void removeApplicationDeviceRelation(String applicationDeviceRelationId){
		this.applicationDeviceMap.remove(applicationDeviceRelationId);

	}

	/**
	 * Getter function to get a particular LocationConnectionRelation from locationConnectionMap
	 * @param locationConnectionRelationId
	 * @return LocationConnectionRelation
	 */
	public LocationConnectionRelation getLocationConnectionRelation(String locationConnectionRelationId)	{
		return this.locationConnectionMap.get(locationConnectionRelationId);
	}

	/**
	 * Function to add a particular locationConnection to locationConnectionMap
	 * @param locationConnection
	 */
	public void addLocationConnectionRelation(LocationConnectionRelation locationConnection){
		this.locationConnectionMap.put(locationConnection.getLocationConnectionRelationId(),locationConnection);
	}

	/**
	 * Function to remove a particular locationConnection from locationConnectionMap
	 * @param locationConnectionRelationId
	 */
	public void removeLocationConnectionRelation(String locationConnectionRelationId){
		this.locationConnectionMap.remove(locationConnectionRelationId);
	}

	/**
	 * Getter function to get a particular DeviceLocationRelation from deviceLocationMap
	 * @param deviceLocationRelationId
	 * @return DeviceLocationRelation
	 */
	public DeviceLocationRelation getDeviceLocationRelation(String deviceLocationRelationId)	{
		return this.deviceLocationMap.get(deviceLocationRelationId);
	}

	/**
	 * Function to add a particular deviceLocation to deviceLocationMap
	 * @param deviceLocation
	 */
	public void addDeviceLocationRelation(DeviceLocationRelation deviceLocation){
		this.deviceLocationMap.put(deviceLocation.getDeviceLocationRelationId(),deviceLocation);
	}
	
	/**
	 * Function to remove a particular deviceLocation to deviceLocationMap
	 * @param deviceLocationRelationId
	 */
	public void removeDeviceLocationRelation(String deviceLocationRelationId){
		this.deviceLocationMap.remove(deviceLocationRelationId);
	}
	
	public void loadNetwork(String readString) throws JsonProcessingException{

		//New object created as the called object cannot be accessed as a whole. Therefore new object is created to update the calling object members with values from json string
		Network clonedObject = new Network();
		
		Map<String, Device> jsonMap = this.mapper.readValue(readString,
			    new TypeReference<Map<String,Device>>(){});
		
		this.deviceMap = jsonMap;
		//this.mapper.readValue(readString,Network.class);
		//copyNetwork(clonedObject);
	}

	public void copyNetwork(Network network) {
		this.deviceMap = network.deviceMap;
		this.connectionMap = network.connectionMap;
		this.locationMap = network.locationMap;
		this.applicationMap = network.applicationMap;
		this.applicationDeviceMap = network.applicationDeviceMap;
		this.locationConnectionMap = network.locationConnectionMap;
		this.deviceLocationMap = network.deviceLocationMap;
	}


	public String saveNetwork() throws InterruptedException{
		try {
			String jsonString = this.mapper.writeValueAsString(this.deviceMap);
			//String jsonString = "{\"deviceMap\" : " + this.mapper.writeValueAsString(this.deviceMap);
//			jsonString = jsonString + ","+ "\"connectionMap\":" + this.mapper.writeValueAsString(this.connectionMap);
//			jsonString = jsonString + ","+ "\"locationMap\":" + this.mapper.writeValueAsString(this.locationMap);
//			jsonString = jsonString + ","+ "\"applicationMap\":" + this.mapper.writeValueAsString(this.applicationMap);
//			jsonString = jsonString + ","+ "\"applicationDeviceMap\":" + this.mapper.writeValueAsString(this.applicationDeviceMap);
//			jsonString = jsonString + ","+ "\"locationConnectionMap\":" + this.mapper.writeValueAsString(this.locationConnectionMap);
//			jsonString = jsonString + ","+ "\"deviceLocationMap\":" + this.mapper.writeValueAsString(this.deviceLocationMap) +"}";
			return jsonString;
		}
		catch(JsonProcessingException e) {
			return null;
		}

	}
	
	
	/**
	 * Function to get location of a particular device instance
	 * @param deviceId
	 * @return Location
	 */
	public Location getCurrentDeviceLocation(Device device) {
		Location location = null;
		if(this.deviceMap.containsValue(device)) {
			location = this.getDeviceLocationRelation(device.getLocationRelationId()).getLocation();  		
		}
		return location;
	}

	/**
	 * Function to get location of a particular application instance
	 * @param applicationId
	 * * @return Location
	 */
	public Location getCurrentApplicationLocation(ApplicationInstance application) {
		Location location = null;
		if (this.applicationMap.containsValue(application)){
			location = this.getDeviceLocationRelation(this.getApplicationInstanceDeviceRelation(application.getDeviceRelationId()).getApplicationDevice().getLocationRelationId()).getLocation();
			//location = this.getDeviceLocationRelation(this.getApplicationInstanceDeviceRelation(this.getApplicationInstance(application.getApplicationType().getApplicationId()).getDeviceRelationId()).getApplicationDevice().getLocationRelationId()).getLocation();
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
		sourceLocation = getCurrentDeviceLocation(this.getDevice(sourceId));
		destinationLocation = getCurrentDeviceLocation(this.getDevice(destinationId));
		double distance = this.calculateDistance(sourceLocation,destinationLocation);
		return distance;
	}

	/**
	 * Function to get physical distance between application instances.
	 * @param sourceId and destinationId
	 */
	public double getphysicalDistanceBetweenApplications(String sourceId, String destinationId) {
		Location sourceLocation,destinationLocation;
		sourceLocation = getCurrentApplicationLocation(this.getApplicationInstance(sourceId));
		destinationLocation = getCurrentApplicationLocation(this.getApplicationInstance(destinationId));
		double distance = this.calculateDistance(sourceLocation,destinationLocation);
		return distance;
	}
	
	/**
	 * Function to get physical distance between locations.
	 * @param sourceId and destinationId
	 */
	public double getphysicalDistanceBetweenlocations(String sourceId, String destinationId) {
		Location sourceLocation,destinationLocation;
		sourceLocation = getLocation(sourceId);
		destinationLocation = getLocation(destinationId);
		double distance = this.calculateDistance(sourceLocation,destinationLocation);
		return distance;
	}

	/**
	 * Function to get connection index from connectionMap using id.
	 * @param id
	 */
//	public int getIndexFromConnectionMap(String id) {
//		int idx=0;
//		for (Object obj : this.connectionMap) {
//			Connection connection = (Connection)obj;
//			if (connection.getConnectionId().equals(id))
//			{
//				idx = this.connectionMap.indexOf(obj);
//			}
//		}
//		return idx;
//	}  
	
	/**
	 * Function to get network distance between devices or between application instances.
	 * @param sourceId and destinationId
	 */
	public double getNetworkDistance(String sourceId, String destinationId) {
//		int sourceidx = this.getIndexFromConnectionMap(sourceId);
//		int destinationidx = this.getIndexFromConnectionMap(destinationId);
//		double distance = 0;
//		for (int i =0;i<connectionMap.size();i++) {
//			if(i>=sourceidx && i<=destinationidx)
//				distance = distance +  this.connectionMap.get(i).getNetworkCapacity().getLatency();
//		}
		return 0;
	}  

}
