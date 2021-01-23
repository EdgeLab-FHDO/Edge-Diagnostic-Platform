package InfrastructureManager.NetworkStructure;
public class LocationConnectionRelation {
	
	
	private DeviceLocationRelation sourceLocation;
	private DeviceLocationRelation destinationLocation;
	
	public LocationConnectionRelation(int sourceDevice, int sourceLocation, int destinationDevice, int destinationLocation) {
		this.sourceLocation = new DeviceLocationRelation(sourceDevice,sourceLocation);
		this.destinationLocation = new DeviceLocationRelation(destinationDevice,destinationLocation);
	}
	
	
	public DeviceLocationRelation getSourceLocation () {
		return this.sourceLocation;	
	}
	
	public DeviceLocationRelation getDestinationLocation () {
		return this.destinationLocation;	
	}
	

	//source - https://www.geodatasource.com/developers/java and https://www.w3resource.com/java-exercises/basic/java-basic-exercise-36.php
	public double calculateDistance(DeviceLocationRelation source,DeviceLocationRelation destination)
	{
		double earthRadius = 6371.01; //Kilometers
		double distance = 0;
		if ((source.getLocation().getLatitude() == destination.getLocation().getLatitude()) && (source.getLocation().getLongitude() == destination.getLocation().getLongitude())) {
			distance =  0;
			}

		else {
			double theta = source.getLocation().getLongitude() - destination.getLocation().getLongitude();
			double sourceLatitudeRadians = Math.toRadians(source.getLocation().getLatitude());
			double destinationLatitudeRadians = Math.toRadians(destination.getLocation().getLatitude());
	        distance = Math.acos(Math.sin(sourceLatitudeRadians)*Math.sin(destinationLatitudeRadians) + Math.cos(sourceLatitudeRadians)*Math.cos(destinationLatitudeRadians)*Math.cos(Math.toRadians(theta)));
	        distance = earthRadius * distance;     
	        }
		return distance;
	}
	
}