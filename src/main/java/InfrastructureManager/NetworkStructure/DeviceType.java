package InfrastructureManager.AdvantEdge.NetworkStructure;

enum TypeId {UE,POA,FOG,EDGE,CLOUD,NONE}
enum Mobility {MOVABLE,NONMOVABLE}

public class DeviceType {
	private TypeId typeIdentifier;
	private Mobility mobilityType;
	private String deviceName = "Device name";
	
	public setTypeId(TypeId typeIdentifier)
    {
        this.typeIdentifier = typeIdentifier;
    }
	
	public setMobility(Mobility mobilityType)
    {
        this.mobilityType = mobilityType;
    }

	public setDeviceName(String deviceName)
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
}

