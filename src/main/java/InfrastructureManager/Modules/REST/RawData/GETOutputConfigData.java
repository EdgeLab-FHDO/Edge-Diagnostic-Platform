package InfrastructureManager.Modules.REST.RawData;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GETOutputConfigData extends RESTIOConfigData {

    public GETOutputConfigData(@JsonProperty("name") String name,
                               @JsonProperty("URL") String URL) {
        super(name, URL);
    }
}
