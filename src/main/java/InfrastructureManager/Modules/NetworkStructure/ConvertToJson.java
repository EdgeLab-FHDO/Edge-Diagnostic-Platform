package InfrastructureManager.Modules.NetworkStructure;

import java.io.File;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class ConvertToJson{

    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        //mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
        Network abcd = newNetwork();

        try {
           
            String jsonString = mapper.writeValueAsString(abcd);
            System.out.println(jsonString);
            mapper.writeValue(new File("testnetwork.json"), abcd);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Network newNetwork() {
        Network abc = new Network();
    	DeviceType devicetype = new DeviceType(DeviceType.TypeId.UE,DeviceType.Mobility.NONMOVABLE,"Vodafone");
		ComputeLimits computelimits = new ComputeLimits(1,2,3);
		Device device1 = new Device("100","192.168.0.1",devicetype,computelimits);

		abc.addDevice(device1);
		
		NetworkLimits networkLimits = new NetworkLimits(0.5f,0.2f,1.5,2.0);
		Connection connection1 = new Connection("Connection1",networkLimits);
		
		abc.addConnection(connection1);
		
		Location location1 = new Location("location1",10.5f,20.5f);
		abc.addLocation(location1);
		
		ComputeRequirements computeRequirements = new ComputeRequirements(1.0f,2.0f,3.0f);
		NetworkRequirements networkRequirements = new NetworkRequirements(10.0f,20.0f,30.0,40.0);
		ApplicationType applicationType = new ApplicationType("Application1",computeRequirements,networkRequirements);
		ApplicationInstance application1 = new ApplicationInstance(applicationType,ApplicationInstance.State.RUNNING);
		ApplicationInstanceDeviceRelation appDevice1 = new ApplicationInstanceDeviceRelation("appDevice1",application1,device1);
		LocationConnectionRelation locCon1 = new LocationConnectionRelation("locCon1",location1,connection1);
		DeviceLocationRelation devLoc1 = new DeviceLocationRelation("devLoc1",device1,location1);
			
		abc.addApplicationInstance(application1);
		abc.addApplicationDeviceRelation(appDevice1);
		abc.addLocationConnectionRelation(locCon1);
		abc.addDeviceLocationRelation(devLoc1);
        
        return abc;
    }
}