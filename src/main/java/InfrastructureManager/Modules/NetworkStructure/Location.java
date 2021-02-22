package InfrastructureManager.Modules.NetworkStructure;


public class Location {
	private String locationId;
	private float latitude;
	private float longitude;
	
	public Location(String locationId,float latitude,float longitude) {
		this.locationId = locationId;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public String getLocationId() {
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
	
	//ToDO - Additional functions might be required later
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
	
}
	
	
	