package Communication;


import Communication.Exception.ErrorInRequestException;
import Communication.Exception.RESTClientException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class RequestMaker {
    private final HttpClient client;
    private final String baseURL;

    public RequestMaker(String baseURL) {
        this.baseURL = baseURL;
        this.client =  HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(20))
                .build();
    }

    public String get(String url) throws RESTClientException {
        String requestURL = this.baseURL + url;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(requestURL))
                .GET().build();
        return this.send(request);
    }

    public void post(String url, String body) throws RESTClientException {
        String requestURL = this.baseURL + url;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(requestURL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        this.send(request);
    }

    private String send(HttpRequest request) throws RESTClientException {
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new ErrorInRequestException(response.statusCode());
            }
            return response.body();
        } catch (IOException | InterruptedException e) {
            throw new RESTClientException("Error while sending request",e);
        }
    }
}
