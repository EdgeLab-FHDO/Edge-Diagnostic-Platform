package InfrastructureManager.NetworkStructure;
import java.util.ArrayList;
import java.util.List;
public class Network {
	
    private List<Device> deviceList;
    private List<Connection> connectionList;
    private List<Location> locationList;
    private List<ApplicationInstance> applicationList;
	
	
	public Network() {
		deviceList = new ArrayList<>();
		connectionList = new ArrayList<>();
		locationList = new ArrayList<>();
		applicationList = new ArrayList<>();
	}
	//ToDO - implement the JSON read
	public void loadNetwork() {
		
	}
	
	//ToDO - implement the JSON write
	public void saveNetwork() {
		
	}
	
	public static void main(String[] args)
	{
		//ToDo - implement the main function
	}	
}
