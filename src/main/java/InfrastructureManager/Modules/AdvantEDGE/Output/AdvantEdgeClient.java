package InfrastructureManager.Modules.AdvantEDGE.Output;

import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;
import InfrastructureManager.ModuleManagement.PlatformOutput;
import InfrastructureManager.Modules.AdvantEDGE.AdvantEdgeModuleObject;
import InfrastructureManager.Modules.AdvantEDGE.Exception.AdvantEdgeModuleException;
import InfrastructureManager.Modules.AdvantEDGE.Exception.ErrorInResponseException;
import InfrastructureManager.Modules.AdvantEDGE.Output.NetworkCharacteristic.NetworkCharacteristicsUpdate;
import InfrastructureManager.Modules.AdvantEDGE.Output.NetworkCharacteristic.NetworkEvent;
import InfrastructureManager.Modules.AdvantEDGE.Output.NetworkCharacteristic.NetworkParameters;
import InfrastructureManager.Utils.FileParser;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.nio.file.Paths;
import java.time.Duration;

/**
 * Class representing interaction with AdvantEdge as a form of PlatformOutput.
 * <p>
 * This type of output is used for sending different request to the deployed AdvantEDGE REST API in order to use its functionalities.
 * <p>
 * It provides the following functionalities:
 * <p>
 * - AdvantEDGE Scenario creation
 * - AdvantEDGE Scenario deployment
 * - AdvantEDGE Scenario termination
 * - AdvantEDGE Scenario network characteristics definition (latency, jitter, throughput and packet loss)
 */
public class AdvantEdgeClient extends AdvantEdgeModuleObject implements PlatformOutput {
    //The controller API is exposed on port 80 & 443 of the node where AdvantEDGE is deployed.
    private final String requestPath;
    private final HttpClient client = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .followRedirects(HttpClient.Redirect.NORMAL)
            .connectTimeout(Duration.ofSeconds(20))
            .build();

    /**
     * Instantiates a new Advant edge client.
     *
     * @param module  Owner module of this output
     * @param name    Name of this output. Normally hardcoded as "MODULE_NAME.out"
     * @param address IP Address for AdvantEdge Deployment
     * @param port    Port where AdvantEdge is exposed in the address
     */
    public AdvantEdgeClient(ImmutablePlatformModule module, String name, String address, int port) {
        super(module, name);
        this.getLogger().debug(this.getName(),"Instantiating a new AdvantEdge client, module: "+module+",name: "+name+",address: "+address+",port: "+port);
        this.requestPath = address + ":" + port;
    }

    /**
     * Based on processed responses from the inputs executes the different functionalities
     * @param response Must be in the way "advantEdge COMMAND" and additionally:
     * - Creating Scenario : Should include the  name of the scenario and the path of the file where the scenario is defined
     * - Deploy Scenario : Should include the name of the scenario to be deployed
     * - Terminate Scenario: Should include the name of the sandbox name
     * - Network Update : Should include the sandbox name, the element name (which receive the update), element type and network params.
     *
     * @throws AdvantEdgeModuleException If an invalid command is passed (not defined), arguments are missing for a command or errors have occurred in the execution or interaction with the API.
     */
    @Override
    public void execute(String response) throws AdvantEdgeModuleException {
        this.getLogger().debug(this.getName(),"AdvantEdge resp: "+response);
        String[] command = response.split(" ");
        if (command[0].equals("advantEdge")) { //The commands must come like "advantEdge command"
            this.getLogger().debug(this.getName(),"AdvantEdge add cmds: "+command[1]);
            try {
                switch (command[1]) {
                    case "create" -> createAEScenario(command[2], command[3]);
                    case "deploy" -> deployAEScenario(command[2]);
                    case "terminate" -> terminateAEScenario(command[2]);
                    case "networkUpdate" -> sendNetworkCharacteristicsUpdateAEScenario(command[2], command[3],
                            command[4], command[5], command[6], command[7], command[8], command[9]);
                    default -> throw new AdvantEdgeModuleException("Invalid command " + command[1]
                            + " for AdvantEdgeClient");
                }
            } catch (IndexOutOfBoundsException e) {
                throw new AdvantEdgeModuleException("Arguments missing in command " + response
                        + " to AdvantEdgeClient", e);
            }
        }
    }

    /**
     * Create an AdvantEDGE scenario and add it to the platform scenario store, using AdvantEDGE REST API
     * @param name name of the scenario to be added
     * @param pathToFile path to the file where the scenario is defined. If it is a YAML file, it parses it into JSON
     * @throws AdvantEdgeModuleException If an error occurs while creating the scenario or parsing the file
     * @throws ErrorInResponseException If a bad response is received from the API when creating the scenario
     */
    private void createAEScenario(String name, String pathToFile) throws AdvantEdgeModuleException {
        this.getLogger().debug(this.getName(),"Creating a new AdvantEdge client, name: "+name+",pathtoFile: "+pathToFile);
        if (pathToFile.endsWith(".yaml")) {
            pathToFile = FileParser.YAMLtoJSON(pathToFile);
        }
        //String requestPath = "https://postman-echo.com/post/";
        String requestPath = this.requestPath + "/platform-ctrl/v1/scenarios/" + name;
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(requestPath))
                    .timeout(Duration.ofMinutes(1))
                    .header("Content-Type", "application/json")
                    .POST(BodyPublishers.ofFile(Paths.get(pathToFile)))
                    .build();
            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new ErrorInResponseException("Response with error code: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            throw new AdvantEdgeModuleException("Error while creating scenario " + name, e);
        }
    }

    /**
     * Deploy a scenario present in the platform scenario store in AdvantEDGE (Using REST API)
     * @param name Name of the scenario to be deployed
     * @throws AdvantEdgeModuleException If an error occur while deploying the scenario
     * @throws ErrorInResponseException If a bad response is received from the API when deploying the scenario
     */
    private void deployAEScenario(String name) throws AdvantEdgeModuleException {
        this.getLogger().debug(this.getName(),"Deploying a scenario present in the platform, name: "+name);
        String requestPath = this.requestPath + "/platform-ctrl/v1/sandboxes/sandbox-" + name;
        //String requestPath = "https://postman-echo.com/post/";
        String jsonRequestString = "{\"scenarioName\" : \""+name+"\"}";

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(requestPath))
                    .timeout(Duration.ofMinutes(1))
                    .header("Content-Type", "application/json")
                    .POST(BodyPublishers.ofString(jsonRequestString))
                    .build();
            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new ErrorInResponseException("Response with error code: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            throw new AdvantEdgeModuleException("Error while deploying scenario " + name, e);
        }
    }

    /**
     * Terminate the current running scenario in AdvantEdge (Using the REST API)
     * @param sandboxName Name of the sandbox where the scenario is deployed ("sandbox-SCENARIO_NAME")
     * @throws AdvantEdgeModuleException If an error occurred while terminating the scenario.
     * @throws ErrorInResponseException If a bad response is received from the API when terminating the scenario
     */
    private void terminateAEScenario(String sandboxName) throws AdvantEdgeModuleException {
        this.getLogger().debug(this.getName(),"Terminating the current running scenario in AdvantEdge, sandboxName: "+sandboxName);
        String requestPath = this.requestPath + "/" + sandboxName + "/sandbox-ctrl/v1/active/";
        //String requestPath = "https://postman-echo.com/delete";
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(requestPath))
                    .timeout(Duration.ofMinutes(1))
                    .DELETE()
                    .build();
            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new ErrorInResponseException("Response with error code: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            throw new AdvantEdgeModuleException("Error while terminating scenario " + sandboxName, e);
        }
    }

    /**
     * Sends an network characteristics update to an already running AdvantEdge scenario (Using the REST API)
     * @param sandboxName Name of the sandbox where the scenario is deployed ("sandbox-SCENARIO_NAME")
     * @param elementName The element name of the node to which the network characteristic update will be applied.
     * @param elementType The type of element: FOG,UE, etc.
     * @param throughputDl Maximum amount of data moved between two points in the network (DownLink direction) (Mbps)
     * @param throughputUl Maximum amount of data moved between two points in the network (UpLink direction) (Mbps)
     * @param latency Amount of time that a packet takes to traverse a network to its final destination (ms)
     * @param latencyVariation Variation of the latency parameter (ms)
     * @param packetLoss Packet loss occurs when data packets travelling across the network fail to reach their destination (%)
     * @throws AdvantEdgeModuleException If an error occurs while sending the NW Characteristics update
     * @throws ErrorInResponseException If a bad response is received from the API when sending the network characteristics update
     */
    private void sendNetworkCharacteristicsUpdateAEScenario(String sandboxName, String elementName, String elementType,
                                                            String throughputDl, String throughputUl, String latency,
                                                            String latencyVariation, String packetLoss)
            throws AdvantEdgeModuleException {
        this.getLogger().debug(this.getName()," - Sending network characteristics update,sandboxName: "+sandboxName+
                ", eleName: "+elementName+", eleType: "+elementType+", ThUL:"+throughputUl+", ThDL: "+throughputDl+
                ", Lateny: "+latency+", LatVariation: "+latencyVariation+", PackLoss: "+packetLoss);
        String requestPath = this.requestPath + "/" + sandboxName
                + "/sandbox-ctrl/v1/events/NETWORK-CHARACTERISTICS-UPDATE";

        NetworkParameters networkParameters = new NetworkParameters(Integer.parseInt(throughputDl),
                Integer.parseInt(throughputUl), Integer.parseInt(latency), Integer.parseInt(latencyVariation),
                Integer.parseInt(packetLoss));

        NetworkEvent networkEvent = new NetworkEvent(elementName, elementType, networkParameters);
        NetworkCharacteristicsUpdate networkCharacteristicsUpdate = new NetworkCharacteristicsUpdate(networkEvent);

        ObjectMapper mapper = new ObjectMapper();

        try {
            String jsonString = mapper.writeValueAsString(networkCharacteristicsUpdate);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(requestPath))
                    .timeout(Duration.ofMinutes(1))
                    .header("Content-Type", "application/json")
                    .POST(BodyPublishers.ofString(jsonString))
                    .build();
            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new ErrorInResponseException("Response with error code: " + response.statusCode());
            }

        } catch (IOException | InterruptedException e) {
            throw new AdvantEdgeModuleException("Error while sending NW Characteristics update to " + sandboxName, e);
        }
    }
}
