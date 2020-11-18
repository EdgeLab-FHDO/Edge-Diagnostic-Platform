package Application.OpenCVClient;

import java.io.IOException;
import java.net.ConnectException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.time.Duration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

public class MasterCommunicator {
    //later move this to configuration or as a paramter
    public String url;
    private final HttpClient client = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .followRedirects(HttpClient.Redirect.NORMAL)
            .connectTimeout(Duration.ofSeconds(20))
            .build();

    public MasterCommunicator(String masterUrl) {
        url = masterUrl;
    }

    public String[] getServer() throws InterruptedException, IOException {
        String[] serverInformation = new String[2];
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofMinutes(1))
                .header("Content-Type", "application/json")
                .GET()
                .build();
        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());
        switch (response.statusCode()) {
            case 200:
                ObjectMapper mapper = new ObjectMapper();
                JsonNode node = mapper.readTree(response.body());
                serverInformation[0] = "IP=" + node.findValue("ipAddress").asText();
                serverInformation[1] = "PORT=" + node.findValue("port").asText();
                break;
            case 400: throw new ConnectException("400 - Bad Request");
            default: throw new ConnectException("404 - Not found");
        }

        return serverInformation;
    }
}