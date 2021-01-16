package InfrastructureManager.NetworkStructure;

public class Location {
	private int locationId;
	private float latitude;
	private float longitude;
	
	public int getLocationId() {
		return locationId;
	}
	
	public float getLatitude() {
		return latitude;
	}
	
	public float getLongitude() {
		return longitude;
	}
	
	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}
	
	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}
	

	public Location(int locationId) {
		this.locationId = locationId;
		this.latitude = 0.0f;
		this.longitude = 0.0f;
	}
	
	public Location(int locationId,float latitude,float longitude) {
		this.locationId = locationId;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	
	//To be moved to different class later and exception handling to be taken care
	public int latitudeLongitudeRangeCheck(Location location)
	{
		int status =0;
		if((rangeCheck(location.latitude,-90,90)) && (rangeCheck(location.longitude,-180,180)))
		{
			status = 1;
		}
		else {
			//ToDO error handling
		}
		return status;
	}
	public boolean rangeCheck(float value,float min,float max)
	{
		boolean validRange = false;
		if (value >= min && value <= max)
		{
			validRange = true;
		}
		return validRange;
	}
	
	//To be moved to different class later
	//source - https://www.geodatasource.com/developers/java and https://www.w3resource.com/java-exercises/basic/java-basic-exercise-36.php
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
}
	
	
	