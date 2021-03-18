package Application.Utilities;

import java.io.IOException;

public class HeartBeatOperator {
    public String url;
    public String body;
    private final RESTHandler handler;
    
    public HeartBeatOperator(String url, String body) {
        this.url = url;
        this.body = body;
        this.handler = new RESTHandler();
    }

    public void beat() throws IOException, InterruptedException, IllegalArgumentException, SecurityException {
        handler.sendPostRequest(url, body);
    }

    public void setBody(String body){
        this.body = body;
    }
}