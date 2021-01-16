package InfrastructureManager.NetworkStructure;
public class LocationConnectionRelation {
	
	//ToDo Implement the relationship functions once it's clear 
	
	public DeviceLocationRelation sourceLocation;
	public DeviceLocationRelation destinationLocation;
	
	public LocationConnectionRelation(int sourceDevice, int sourceLocation, int destinationDevice, int destinationLoction) {
		this.sourceLocation = new DeviceLocationRelation(sourceDevice,sourceLocation);
		this.destinationLocation = new DeviceLocationRelation(destinationDevice,destinationLoction);
	}
	
}