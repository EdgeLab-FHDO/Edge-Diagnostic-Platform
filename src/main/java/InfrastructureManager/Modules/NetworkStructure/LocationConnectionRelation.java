package InfrastructureManager.Modules.NetworkStructure;
public class LocationConnectionRelation {
	
	private Connection connection;
	private Location location;
	
	public LocationConnectionRelation(Connection connection,Location location) {
		this.connection = connection;
		this.location = location;
	}
	
	public Location getLocationRelation () {
		return this.location;	
	}
	
	public Connection getConnectionRelation () {
		return this.connection;	
	}
	

	//ToDo move it to other file with other function implementation
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