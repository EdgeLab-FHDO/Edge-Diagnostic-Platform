package InfrastructureManager.NetworkStructure;


/**
 * This class contains location information.
 *
 * @author Shankar Lokeshwara
 */

public class Location {
	private int locationId;
	private float latitude;
	private float longitude;
	
	/**
	* Default constructor for Location Class
	*/
	public Location() {
		this.locationId = 0;
		this.latitude = 0.0f;
		this.longitude = 0.0f;
	}
	
	/**
	* Parameterized constructor for Location Class
	* @param locationId 				
	*/
	public Location(int locationId) {
		this.locationId = locationId;
		this.latitude = 0.0f;
		this.longitude = 0.0f;
	}
	
	/**
	* Parameterized constructor for Location Class
	* @param locationId 				
	* @param latitude
	* @param longitude
	*/
	public Location(int locationId,float latitude,float longitude) {
		this.locationId = locationId;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	/**
	 * Getter function
	 * @return locationId
	 */
	public int getLocationId() {
		return locationId;
	}
	
	/**
	 * Getter function
	 * @return latitude
	 */
	
	public float getLatitude() {
		return latitude;
	}
	
	/**
	 * Getter function
	 * @return longitude
	 */
	
	public float getLongitude() {
		return longitude;
	}
	
	/**
	 * Setter function
	 * @param latitude
	 */
	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}
	
	/**
	 * Setter function
	 * @param longitude
	 */
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
	
	
	