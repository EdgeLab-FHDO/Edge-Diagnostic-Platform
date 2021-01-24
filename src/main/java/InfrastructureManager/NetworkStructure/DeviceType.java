package InfrastructureManager.NetworkStructure;

/**
 * This class contains device types.
 *
 * @author Shankar Lokeshwara
 */

public class DeviceType {
	enum TypeId {UE,POA,FOG,EDGE,CLOUD,NONE} //Types of the device ID's
	enum Mobility {MOVABLE,NONMOVABLE,NONE} //Types of devices based on mobility
	
	private TypeId typeIdentifier;
	private Mobility mobilityType;
	private String deviceName;
	
	/**
	* Parameterized constructor for DeviceType Class
	* @param TypeId 				
	* @param mobilityType
	* @param deviceName
	*/
	public DeviceType(TypeId typeIdentifier,Mobility mobilityType,String deviceName) {
		this.typeIdentifier = typeIdentifier;
		this.mobilityType = mobilityType;
		this.deviceName = deviceName;
	}
	/**
	 * Getter function
	 * @return typeIdentifier
	 */
	public TypeId getTypeId()
    {
        return typeIdentifier;
    }
	/**
	 * Getter function
	 * @return mobilityType
	 */
	public Mobility getMobility()
    {
        return mobilityType;
    }	
	/**
	 * Getter function
	 * @return deviceName
	 */
	public String getDeviceName() {
        return deviceName;
    }
	
}

