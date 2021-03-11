package Application.MarkerDetection.OpenCVClient;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

//TODO refactor the sender into utilities
public class LatencyReporterOperator {
    public String url;
    private final HttpClient client = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .followRedirects(HttpClient.Redirect.NORMAL)
            .connectTimeout(Duration.ofSeconds(20))
            .build();

    public LatencyReporterOperator(String url) {
        this.url = url;
    }

    public void report(String body) throws IOException, InterruptedException, IllegalArgumentException, SecurityException {
        if(body!=null && !body.isEmpty()) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofMinutes(1))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();
            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());
            switch (response.statusCode()) {
                case 200:
                    break;
                case 400:
                    throw new ConnectException("400 - Bad Request");
                default:
                    throw new ConnectException("404 - Not found");
            }
        }
    }
}
