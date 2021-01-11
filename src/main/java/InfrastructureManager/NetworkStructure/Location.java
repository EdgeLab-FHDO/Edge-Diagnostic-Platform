package InfrastructureManager.AdvantEdge.NetworkStructure;

public class Location {
	private int locationId = 0;
	private float latitude = 0.0;
	private float longitude = 0.0;
	
	public int getLocationId() {
		return locationId;
	}
	
	public float getLatitude() {
		return latitude;
	}
	
	public float getLongitude() {
		return longitude;
	}
	
	public setLatitude(float latitude) {
		this.latitude = latitude;
	}
	
	public setLongitude(float longitude) {
		this.longitude = longitude;
	}
	
	public setLocationId(int locationId) {
		this.locationId = locationId;
	}
	
}
	
	