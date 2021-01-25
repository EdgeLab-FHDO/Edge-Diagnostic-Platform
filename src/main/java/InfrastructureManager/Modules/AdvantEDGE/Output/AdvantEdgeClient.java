package InfrastructureManager.Modules.AdvantEDGE.Output;

import InfrastructureManager.ModuleManagement.PlatformModule;
import InfrastructureManager.Modules.AdvantEDGE.Exception.AdvantEdgeModuleException;
import InfrastructureManager.Modules.AdvantEDGE.Exception.ErrorInResponseException;
import InfrastructureManager.Modules.AdvantEDGE.Output.NetworkCharacteristic.NetworkCharacteristicsUpdate;
import InfrastructureManager.Modules.AdvantEDGE.Output.NetworkCharacteristic.NetworkEvent;
import InfrastructureManager.Modules.AdvantEDGE.Output.NetworkCharacteristic.NetworkParameters;
import InfrastructureManager.ModuleManagement.ModuleOutput;
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

public class AdvantEdgeClient extends ModuleOutput {
    //The controller API is exposed on port 80 & 443 of the node where AdvantEDGE is deployed.
    private final String requestPath;
    private final HttpClient client = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .followRedirects(HttpClient.Redirect.NORMAL)
            .connectTimeout(Duration.ofSeconds(20))
            .build();

    public AdvantEdgeClient(PlatformModule module, String name, String address, int port) {
        super(module, name);
        this.requestPath = address + ":" + port;
    }

    /**
     * Based on responses from the master executes the different functionalities
     * @param response Must be in the way "advantEdge command" and additionally:
     *      *                 - Creating Scenario : Should include the  name of the scenario and the path of the file
     *                          where the scenario is defined
     *      *                 - Deploy Scenario : Should include the name of the scenario to be deployed
     *      *                 - Terminate Scenario: Should include the name of the sandbox name
     *      *                 - Network Update : Should include the sandbox name, the element name (which receives
     *                          the update), element type and network params.
     * @throws IllegalArgumentException If the command is not defined or is missing arguments
     */
    @Override
    public void execute(String response) throws AdvantEdgeModuleException {
        String[] command = response.split(" ");
        if (command[0].equals("advantEdge")) { //The commands must come like "advantEdge command"
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
     */
    private void createAEScenario(String name, String pathToFile) throws AdvantEdgeModuleException {
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
     */
    private void deployAEScenario(String name) throws AdvantEdgeModuleException {
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
     */
    private void terminateAEScenario(String sandboxName) throws AdvantEdgeModuleException {
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
            throw new AdvantEdgeModuleException("Error while deploying scenario " + sandboxName, e);
        }
    }

    /**
     * Sends an network characteristics update to an already running AdvantEdge scenario (Using the REST API)
     * @param elementName The element name of the node to which the network characteristic update will be applied.
     * @param elementType The type of element: FOG,
     */
    private void sendNetworkCharacteristicsUpdateAEScenario(String sandboxName, String elementName, String elementType,
                                                            String throughputDl, String throughputUl, String latency,
                                                            String latencyVariation, String packetLoss)
            throws AdvantEdgeModuleException {
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
            throw new AdvantEdgeModuleException("Error while deploying scenario " + sandboxName, e);
        }
    }
}
