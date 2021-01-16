package InfrastructureManager.NetworkStructure;
enum TypeId {UE,POA,FOG,EDGE,CLOUD,NONE}
enum Mobility {MOVABLE,NONMOVABLE,NONE}

public class DeviceType {
	private TypeId typeIdentifier;
	private Mobility mobilityType;
	private String deviceName;
	
	public void setTypeId(TypeId typeIdentifier)
    {
        this.typeIdentifier = typeIdentifier;
    }
	
	public void setMobility(Mobility mobilityType)
    {
        this.mobilityType = mobilityType;
    }

	public void setDeviceName(String deviceName)
    {
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
	
	public DeviceType() {
		this.typeIdentifier = TypeId.NONE;
		this.mobilityType = Mobility.NONE;
		this.deviceName = "";
	}
	
	public DeviceType(TypeId typeIdentifier,Mobility mobilityType,String deviceName) {
		this.typeIdentifier = typeIdentifier;
		this.mobilityType = mobilityType;
		this.deviceName = deviceName;
	}
}

