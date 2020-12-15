package InfrastructureManager.Configuration.RawData;

import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.HashMap;
import java.util.Map;


public class CustomCommandIO extends IOConfigData {
    private String command;
    private Map<String,String> information;

    public CustomCommandIO() {
        super();
        this.command = null;
        this.information = new HashMap<>();
    }

    public String getCommand() {
        return command;
    }

    public Map<String, String> getInformation() {
        return information;
    }
}
