package Application.Utilities;

import Application.Commons.CoreOperator;

import java.io.IOException;

public class RegistrationOperator {
    public String url;
    public String body;
    private final RESTHandler handler;
    private final CoreOperator activeOperator;

    public RegistrationOperator(CoreOperator operator, String url, String body) {
        this.url = url;
        this.body = body;
        this.handler = new RESTHandler();
        this.activeOperator = operator;
    }

    public void register() throws IOException, InterruptedException, IllegalArgumentException, SecurityException {
        System.out.println("trying to register");
        handler.sendPostRequest(url, body);
        activeOperator.startBeater();
    }
}
