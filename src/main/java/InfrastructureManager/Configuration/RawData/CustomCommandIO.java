package InfrastructureManager.Configuration.RawData;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;


public class CustomCommandIO extends IOConfigData {
    private final String command;
    private final List<String> information;

    @JsonCreator
    public CustomCommandIO(@JsonProperty("command") String command) {
        super();
        this.command = command;
        this.information = new ArrayList<>();
    }

    public String getCommand() {
        return command;
    }

    public List<String> getInformation() {
        return this.information;
    }
}
