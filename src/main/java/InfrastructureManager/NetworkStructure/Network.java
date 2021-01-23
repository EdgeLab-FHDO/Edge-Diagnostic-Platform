package InfrastructureManager.NetworkStructure;
import java.util.ArrayList;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;

/**
 * This class contains relationship between application and device information.
 *
 * @author Shankar Lokeshwara
 */

public class Network {
	
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
		this.deviceList = clonedObject.deviceList;
		this.connectionList = clonedObject.connectionList;
		this.locationList = clonedObject.locationList;
		this.applicationList = clonedObject.applicationList;
		this.applicationDeviceList = clonedObject.applicationDeviceList;
		this.locationConnectionList = clonedObject.locationConnectionList;
		this.deviceLocationList = clonedObject.deviceLocationList;
	}
	
	/**
	 * Function to save JSON string
	 * @return String
	 */
	public String saveNetwork() throws JsonProcessingException{
		String json = null;
		try {
			// convert network object to JSON
			json = this.mapper.writeValueAsString(this);
		} catch (Exception ex) {
		    ex.printStackTrace();
		}
		return json;
		
	}
	
	/**
	 * Getter function
	 * @return deviceList
	 */
    public List<Device> getDeviceList() {
        return deviceList;
    }
    
	/**
	 * Getter function
	 * @return connectionList
	 */
    public List<Connection> getConnectionList() {
        return connectionList;
    }
	
	/**
	 * Getter function
	 * @return locationList
	 */
    public  List<Location> getLocationList() {
        return locationList;
    }
    
	/**
	 * Getter function
	 * @return applicationList
	 */
    public List<ApplicationInstance> getApplicationList() {
        return applicationList;
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
	 * @param deviceList
	 */
    public  void setDeviceList(List<Device> deviceList) {
        this.deviceList = deviceList;
    }

	/**
	 * Setter function
	 * @param connectionList
	 */
    public void setConnectionList(List<Connection> connectionList) {
        this.connectionList = connectionList;
    }
    
	/**
	 * Setter function
	 * @param locationList
	 */
    public  void setLocationList(List<Location> locationList) {
        this.locationList = locationList;
    }
	
	/**
	 * Setter function
	 * @param applicationList
	 */
    public void setApplicationList(List<ApplicationInstance> applicationList) {
        this.applicationList = applicationList;
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
