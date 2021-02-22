package InfrastructureManager.Modules.REST.RawData;

import InfrastructureManager.ModuleManagement.ModuleFactory.ModuleType;
import InfrastructureManager.ModuleManagement.RawData.ModuleConfigData;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Raw data representing a RESTModule.
 * <p>
 * To create a REST module the following data is needed:
 * - Name of the module
 * - Port where the REST server will be deployed
 * - Base URL for all requests
 * - List of {@link POSTInputConfigData} representing the different POST inputs declared.
 * - List of {@link GETOutputConfigData} representing the different GET outputs declared.
 * <p>
 * Type is automatically selected as {@link ModuleType}.REST
 */
public class RESTModuleConfigData extends ModuleConfigData {

    private final int port;
    private final String baseURL;
    private final List<POSTInputConfigData> inputs;
    private final List<GETOutputConfigData> outputs;

    /**
     * Creates a new raw REST module. Uses the {@link JsonProperty} annotation for extracting information from the
     * "name", "port", "baseURL", "POST" and "GET" fields while deserializing.
     *
     * @param name    Name of the module
     * @param port    Port where the REST server will be deployed
     * @param baseURL Base URL for all requests
     * @param inputs  List of raw POST input data representing the different POST inputs declared.
     * @param outputs List of raw GET output data representing the different GET outputs declared.
     */
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

    /**
     * Returns the defined port for the REST server.
     *
     * @return Port for the rest server
     */
    public int getPort() {
        return port;
    }

    /**
     * Returns the defined base URL.
     *
     * @return Base URL for all requests
     */
    public String getBaseURL() {
        return baseURL;
    }

    /**
     * Returns the raw data of the defined inputs.
     *
     * @return List of raw POST input data representing the different POST inputs declared.
     */
    public List<POSTInputConfigData> getPOSTInputs() {
        return inputs;
    }

    /**
     * Returns the raw data of the defined outputs.
     *
     * @return List of raw GET output data representing the different GET outputs declared.
     */
    public List<GETOutputConfigData> getGETOutputs() {
        return outputs;
    }
}
