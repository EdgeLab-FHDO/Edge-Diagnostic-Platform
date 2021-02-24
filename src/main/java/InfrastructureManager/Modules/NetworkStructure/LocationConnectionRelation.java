package InfrastructureManager.Modules.NetworkStructure;
/**
 * This class contains relationship between location and connection information.
 *
 * @author Shankar Lokeshwara
 */
public class LocationConnectionRelation {
	private String locationConnectionRelationId;
	private Connection connection;
	private Location location;

	/**
	 * Parameterized constructor for LocationConnectionRelation Class
	 * @param locationConnectionRelationId
	 * @param connection 	
	 * @param location 			
	 */
	public LocationConnectionRelation(String locationConnectionRelationId,Location location,Connection connection) {
		this.locationConnectionRelationId = locationConnectionRelationId;
		this.connection = connection;
		this.connection.setLocationRelationId(locationConnectionRelationId);
		this.location = location;
		this.location.setConnectionRelationId(locationConnectionRelationId);
	}

	/**
	 * Getter function
	 * @return locationConnectionRelationId
	 */
	public String getLocationConnectionRelationId () {
		return this.locationConnectionRelationId;	
	}
	
	/**
	 * Getter function
	 * @return location
	 */
	public Location getLocationRelation () {
		return this.location;	
	}

	/**
	 * Getter function
	 * @return connection
	 */
	public Connection getConnectionRelation () {
		return this.connection;	
	}

}