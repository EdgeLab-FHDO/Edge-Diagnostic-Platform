package InfrastructureManager.Modules.REST.RawData;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class POSTInputConfigData extends RESTIOConfigData {
    private final String command;
    private final List<String> information;

    @JsonCreator
    public POSTInputConfigData(@JsonProperty("name") String name,
                               @JsonProperty("URL") String URL,
                               @JsonProperty("command") String command) {
        super(name,URL);
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
