package Application.OpenCVClient;

import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpClient.*;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.*;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.*;
import java.net.URI;
import java.time.Duration;
import java.util.Iterator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
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

    public String[] getServer() {
        String[] serverInformation = new String[2];
        try {
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
                    System.out.println("200 - OK");
                    break;
                case 400:
                    System.out.println("400 - Bad Request");
                    break;
                default:
                    System.out.println("404 - Not found");
            }

            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(response.body());
            serverInformation[0] = "IP=" + node.findValue("ipAddress").asText();
            serverInformation[1] = "PORT=" + node.findValue("port").asText();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return serverInformation;
    }
}