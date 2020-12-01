package InfrastructureManager.AdvantEdge;

import InfrastructureManager.AdvantEdge.NetworkCharacteristic.NetworkCharacteristicsUpdate;
import InfrastructureManager.AdvantEdge.NetworkCharacteristic.NetworkEvent;
import InfrastructureManager.AdvantEdge.NetworkCharacteristic.NetworkParameters;
import InfrastructureManager.MasterOutput;
import InfrastructureManager.Utils.FileParser;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.nio.file.Paths;
import java.time.Duration;

public class AdvantEdgeClient extends MasterOutput {
    private final String requestPath; //The controller API is exposed on port 80 & 443 of the node where AdvantEDGE is deployed.
    private final HttpClient client = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .followRedirects(HttpClient.Redirect.NORMAL)
            .connectTimeout(Duration.ofSeconds(20))
            .build();

    public AdvantEdgeClient(String name,String address, int port) {
        super(name);
        this.requestPath = address + ":" + port;
    }

    /**
     * Based on responses from the master executes the different functionalities
     * @param response Must be in the way "advantEdge command" and additionally:
     *      *                 - Creating Scenario : Should include the  name of the scenario and the path of the file where the scenario is defined
     *      *                 - Deploy Scenario : Should include the name of the scenario to be deployed
     *      *                 - Terminate Scenario: Should include the name of the sandbox name
     *      *                 - Network Update : Should include the sandbox name, the element name (which receives the update), element type and network params.
     * @throws IllegalArgumentException If the command is not defined or is missing arguments
     */
    @Override
    public void out(String response) throws IllegalArgumentException {
        String[] command = response.split(" ");
        if (command[0].equals("advantEdge")) { //The commands must come like "advantEdge command"
            try {
                switch (command[1]) {
                    case "create" :
                        createAEScenario(command[2], command[3]);
                        break;
                    case "deploy" :
                        deployAEScenario(command[2]);
                        break;
                    case "terminate" :
                        terminateAEScenario(command[2]);
                        break;
                    case "networkUpdate":
                        sendNetworkCharacteristicsUpdateAEScenario(command[2], command[3], command[4], command[5],
                                command[6], command[7], command[8], command[9]);
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid command for AdvantEdgeClient");
                }
            } catch (IndexOutOfBoundsException e) {
                throw new IllegalArgumentException("Arguments missing for command - AdvantEdgeClient");
            }
        }
    }

    /**
     * Create an AdvantEDGE scenario and add it to the platform scenario store, using AdvantEDGE REST API
     * @param name name of the scenario to be added
     * @param pathToFile path to the file where the scenario is defined. If it is a YAML file, it parses it into JSON
     */
    private void createAEScenario(String name, String pathToFile) {
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
            switch (response.statusCode()) {
                case 200:
                    System.out.println("200 - OK");
                    break;
                case 400:
                    System.out.println("400 - Bad Request");
                    break;
                default:
                    System.out.println("404 - Not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Deploy a scenario present in the platform scenario store in AdvantEDGE (Using REST API)
     * @param name Name of the scenario to be deployed
     */
    private void deployAEScenario(String name) {
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
            switch (response.statusCode()) {
                case 200:
                    System.out.println("200 - OK");
                    break;
                case 400:
                    System.out.println("400 - Bad Request");
                    break;
                default:
                    System.out.println("404 - Not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Terminate the current running scenario in AdvantEdge (Using the REST API)
     */
    private void terminateAEScenario(String sandboxName) {
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
            switch (response.statusCode()) {
                case 200:
                    System.out.println("200 - OK");
                    break;
                default:
                    System.out.println("404 - Not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends an network characteristics update to an already running AdvantEdge scenario (Using the REST API)
     * @param elementName The element name of the node to which the network characteristic update will be applied.
     * @param elementType The type of element: FOG,
     */
    private void sendNetworkCharacteristicsUpdateAEScenario(String sandboxName, String elementName, String elementType, String throughputDl,
                                                            String throughputUl, String latency, String latencyVariation,
                                                            String packetLoss)
    {
        String requestPath = this.requestPath + "/" + sandboxName + "/sandbox-ctrl/v1/events/NETWORK-CHARACTERISTICS-UPDATE";

        NetworkParameters networkParameters = new NetworkParameters(Integer.parseInt(throughputDl), Integer.parseInt(throughputUl),
                Integer.parseInt(latency), Integer.parseInt(latencyVariation), Integer.parseInt(packetLoss));

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
            switch (response.statusCode()) {
                case 200:
                    System.out.println("200 - OK");
                    break;
                case 400:
                    System.out.println("400 - Bad Request");
                    break;
                default:
                    System.out.println("404 - Not found");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
