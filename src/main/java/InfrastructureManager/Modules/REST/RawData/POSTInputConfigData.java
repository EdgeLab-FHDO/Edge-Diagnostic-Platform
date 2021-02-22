package InfrastructureManager.Modules.REST.RawData;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Raw data class that represents a POST input in a REST Module.
 * <p>
 * To define a POSTInput the following data is needed:
 * - Name of the input
 * - URL of the input
 * - Command that the input should return to the platform when read
 * - Arguments to parse from the request body into the command (Not always the case)
 *
 * There are two possible configurations according to the desired behavior, which are specified using the command and information fields:
 *
 * - Pass the entire JSON request body
 * - Extract information (field values) from the body and pass it
 *
 * @see <a href="https://github.com/EdgeLab-FHDO/Edge-Diagnostic-Platform/wiki/Configuring-REST-Input">Wiki Entry</a>
 */
public class POSTInputConfigData extends RESTIOConfigData {
    private final String command;
    private final List<String> information;

    /**
     * Constructor of the class. Creates a new raw POSTInput. Uses the {@link JsonProperty} annotation for extracting information from the
     * "name", "URL", "command" and "information" fields while deserializing.
     *
     * @param name        Name of the input
     * @param URL         URL of the input
     * @param command     Command that the input should return to the platform when read
     * @param information Arguments to parse from the request body into the command.
     */
    @JsonCreator
    public POSTInputConfigData(@JsonProperty("name") String name,
                               @JsonProperty("URL") String URL,
                               @JsonProperty("command") String command,
                               @JsonProperty("information") List<String> information) {
        super(name,URL);
        this.command = command;
        this.information = information;
    }

    /**
     * Returns the defined command for this input
     *
     * @return Defined command for the input
     */
    public String getCommand() {
        return command;
    }

    /**
     * Returns the information about arguments to parse from the request body into the command
     *
     * @return Argument list that should be parsed from the request body into the command.
     */
    public List<String> getInformation() {
        return this.information;
    }
}
