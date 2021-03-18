package Application.MarkerDetection.OpenCVClient;

import Application.Utilities.RESTHandler;

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
    private final RESTHandler handler;

    public LatencyReporterOperator(String url) {
        this.url = url;
        this.handler = new RESTHandler();
    }

    public void report(String body) throws IOException, InterruptedException, IllegalArgumentException, SecurityException {
        if(body!=null && !body.isEmpty()) {
            handler.sendPostRequest(url, body);
        }
    }
}
