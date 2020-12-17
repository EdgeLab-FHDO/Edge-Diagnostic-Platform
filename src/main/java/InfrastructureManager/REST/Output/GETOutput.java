package InfrastructureManager.REST.Output;

import InfrastructureManager.MasterOutput;
import InfrastructureManager.REST.RestServerRunner;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.*;

import static spark.Spark.get;

public class GETOutput extends MasterOutput {

    private Queue<String> toSend;
    private final String path;
    private boolean isActivated;

    private final Route GETHandler = (Request request, Response response) -> {
        response.type("application/json");
        try {
            return this.toSend.remove();
        } catch (NoSuchElementException e) {
            return "";
        }
    };

    public GETOutput(String name, String path) {
        super(name);
        this.toSend = new ArrayDeque<>();
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
        this.toSend.offer(jsonBody);
    }
}
