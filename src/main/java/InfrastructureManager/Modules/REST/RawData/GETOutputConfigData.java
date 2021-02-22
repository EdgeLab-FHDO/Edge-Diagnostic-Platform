package InfrastructureManager.Modules.REST.RawData;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Raw data class that represents a GET output in a REST Module.
 *
 * To define a GETOutput the following data is needed:
 * - Name of the output
 * - URL of the output
 */
public class GETOutputConfigData extends RESTIOConfigData {

    /**
     * Constructor of the class. Creates a new raw GETOutput. Uses the {@link JsonProperty} annotation for extracting information from the
     * "name" and "URL" fields while deserializing.
     *
     * @param name Name of the output
     * @param URL  URL of the output
     */
    public GETOutputConfigData(@JsonProperty("name") String name,
                               @JsonProperty("URL") String URL) {
        super(name, URL);
    }
}
