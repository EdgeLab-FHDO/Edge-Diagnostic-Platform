package InfrastructureManager.NewREST;

import InfrastructureManager.MasterOutput;
import InfrastructureManager.Rest.RestRunner;
import spark.*;

import java.util.EmptyStackException;
import java.util.Stack;

import static spark.Spark.get;

public class toGET extends MasterOutput {

    private final Stack<String> toSend;
    private final String path;
    private boolean isActivated;

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
            if (RestServerRunner.getRestServerRunner().isRunning()) {
                get(this.path,(Request request, Response response) -> {
                    response.type("application/json");
                    try {
                        return this.toSend.pop();
                    } catch (EmptyStackException e) {
                        return "";
                    }
                });
                this.isActivated = true;
            } else {
                throw new IllegalStateException("Rest Server is not running");
            }
        } catch (IllegalStateException e) {
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
