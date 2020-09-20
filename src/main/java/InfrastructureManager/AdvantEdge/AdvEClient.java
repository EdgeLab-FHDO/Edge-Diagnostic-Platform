package InfrastructureManager.AdvantEdge;

import InfrastructureManager.MasterOutput;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.nio.file.Paths;
import java.time.Duration;

public class AdvEClient implements MasterOutput {
    private final HttpClient client = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .followRedirects(HttpClient.Redirect.NORMAL)
            .connectTimeout(Duration.ofSeconds(20))
            .build();

    /**
     * Based on responses from the master executes the different functionalities
     * @param response Must be in the way "advantEdge command" and additionally:
     *                 - Creating Scenario : Should include the  name of the scenario and the path of the file where the scenario is defined
     *                 - Deploy Scenario : Should include the name of the scenario to be deployed
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
                        terminateAEScenario();
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid command for AdvEClient");
                }
            } catch (IndexOutOfBoundsException e) {
                throw new IllegalArgumentException("Arguments missing for command - AdvEClient");
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
            pathToFile = AdvEScenarioParser.parse(pathToFile);
        }
        //String requestPath = "https://postman-echo.com/post/";
        //The controller API is exposed on port 80 & 443 of the node where AdvantEDGE is deployed.
        String requestPath = "http://localhost:10500/scenarios/" + name;
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
        String requestPath = "http://localhost:10500/active/" + name;
        //String requestPath = "https://postman-echo.com/post/";
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(requestPath))
                    .timeout(Duration.ofMinutes(1))
                    .header("Content-Type", "application/json")
                    .POST(BodyPublishers.noBody())
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
    private void terminateAEScenario() {
        String requestPath = "http://localhost:10500/active";
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
}
