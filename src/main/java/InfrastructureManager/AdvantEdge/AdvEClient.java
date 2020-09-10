package InfrastructureManager.AdvantEdge;

import InfrastructureManager.MasterOutput;

import java.io.FileNotFoundException;
import java.io.IOException;
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

    private void createAEScenario(String name, String pathToJSON) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://postman-echo.com/post/"))
                    .timeout(Duration.ofMinutes(1))
                    .header("Content-Type", "application/json")
                    .POST(BodyPublishers.ofFile(Paths.get(pathToJSON)))
                    .build();
            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.statusCode());
            System.out.println(response.body());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //String requestPath = "/scenarios/" + name;
        //System.out.println("POST to " + requestPath + " with body from " + pathToJSON);
    }

    private void deployAEScenario(String name) {
        String requestPath = "/active/" + name;
        System.out.println("POST to " + requestPath);
    }

    private void terminateAEScenario() {
        //TODO: Check the active scenario first
        String requestPath = "/active";
        System.out.println("DELETE to " + requestPath);
    }
}
