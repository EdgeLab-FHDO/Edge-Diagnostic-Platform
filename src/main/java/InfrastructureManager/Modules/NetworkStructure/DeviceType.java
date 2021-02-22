package InfrastructureManager.Modules.NetworkStructure;

public class DeviceType {
	enum TypeId {UE,POA,FOG,EDGE,CLOUD,NONE} //Types of the device ID's
	enum Mobility {MOVABLE,NONMOVABLE,NONE} //Types of devices based on mobility
	
	private TypeId typeIdentifier;
	private Mobility mobilityType;
	private String deviceName;
	
	public DeviceType(TypeId typeIdentifier,Mobility mobilityType,String deviceName) {
		this.typeIdentifier = typeIdentifier;
		this.mobilityType = mobilityType;
		this.deviceName = deviceName;
	}
	public TypeId getTypeId()
    {
        return typeIdentifier;
    }
	public Mobility getMobility()
    {
        return mobilityType;
    }	
	public String getDeviceName() {
        return deviceName;
    }
	
}

