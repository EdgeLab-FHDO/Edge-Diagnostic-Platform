package InfrastructureManager.Modules.REST.RawData;

import InfrastructureManager.ModuleManagement.ModuleFactory.ModuleType;
import InfrastructureManager.ModuleManagement.RawData.ModuleConfigData;
import InfrastructureManager.Modules.REST.Output.GETOutput;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class RESTModuleConfigData extends ModuleConfigData {

    private final int port;
    private final String baseURL;
    private final List<POSTInputConfigData> inputs;
    private final List<GETOutputConfigData> outputs;

    @JsonCreator
    public RESTModuleConfigData(@JsonProperty("name") String name, @JsonProperty("port") int port,
                                @JsonProperty("baseURL") String baseURL,
                                @JsonProperty("POST") List<POSTInputConfigData> inputs,
                                @JsonProperty("GET") List<GETOutputConfigData> outputs) {
        super(name);
        this.type = ModuleType.REST;
        this.port = port;
        this.baseURL = baseURL;
        this.inputs = inputs;
        this.outputs = outputs;
    }

    public int getPort() {
        return port;
    }

    public String getBaseURL() {
        return baseURL;
    }

    public List<POSTInputConfigData> getPOSTInputs() {
        return inputs;
    }

    public List<GETOutputConfigData> getGETOutputs() {
        return outputs;
    }
}
