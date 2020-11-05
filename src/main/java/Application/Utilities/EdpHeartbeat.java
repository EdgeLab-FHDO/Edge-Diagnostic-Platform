import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpRequest.BodyPublishers.*;
import java.util.Iterator;

public class EdpHeartbeat {
    public String masterUrl = "http://127.0.0.1:4567/";
    private final HttpClient client = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .followRedirects(HttpClient.Redirect.NORMAL)
            .connectTimeout(Duration.ofSeconds(20))
            .build();

    public void beat(String name) {
        try {
            System.out.println("beats");

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(masterUrl))
                    .timeout(Duration.ofMinutes(1))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(name))
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