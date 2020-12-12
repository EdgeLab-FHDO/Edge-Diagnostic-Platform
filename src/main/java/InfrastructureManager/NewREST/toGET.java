package InfrastructureManager.NewREST;

import InfrastructureManager.MasterOutput;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.EmptyStackException;
import java.util.Stack;

import static spark.Spark.get;

public class toGET extends MasterOutput {

    private Stack<String> toSend;
    private final String path;
    private boolean isActivated;

    private final Route GETHandler = (Request request, Response response) -> {
        response.type("application/json");
        try {
            return this.toSend.pop();
        } catch (EmptyStackException e) {
            return "";
        }
    };

    public toGET(String name, String path) {
        super(name);
        this.toSend = new Stack<>();
        this.path = path;
        this.isActivated = false;
    }

    @Override
    public void out(String response) throws IllegalArgumentException {
        String[] command = response.split(" ",3);
        if (command[0].equals("toGet")) {
            try {
                switch (command[1]) {
                    case "resource":
                        addResource(command[2]);
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid command for REST");
                }
            } catch (IndexOutOfBoundsException e){
                throw new IllegalArgumentException("Arguments missing for command - REST");
            }
        }
    }

    private void activate() {
        try {
            while (!RestServerRunner.getInstance().isRunning()) { //Wait for the REST server to run
                synchronized (RestServerRunner.ServerRunning) {
                    RestServerRunner.ServerRunning.wait();
                }
            }
            get(this.path, this.GETHandler);
            this.isActivated = true;
        } catch (IllegalStateException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void addResource(String jsonBody) {
        if (!this.isActivated) {
            this.activate();
        }
        this.toSend.push(jsonBody);
    }
}
