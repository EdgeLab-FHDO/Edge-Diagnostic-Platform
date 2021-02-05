package InfrastructureManager.Modules.NetworkStructure;
/**
 * This class contains relationship between location and connection information.
 *
 * @author Shankar Lokeshwara
 */
public class LocationConnectionRelation {
	
	private Connection connection;
	private Location location;
	
	/**
	* Parameterized constructor for LocationConnectionRelation Class
	* @param connection 	
	* @param location 			
	*/
	public LocationConnectionRelation(Connection connection,Location location) {
		this.connection = connection;
		this.location = location;
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