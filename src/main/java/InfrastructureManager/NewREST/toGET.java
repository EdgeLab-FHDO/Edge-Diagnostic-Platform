package InfrastructureManager.NewREST;

import InfrastructureManager.MasterOutput;
import spark.*;

import java.util.EmptyStackException;
import java.util.Stack;

public class toGET extends MasterOutput {

    private final Stack<String> toSend;

    public toGET(String name, String path) {
        super(name);
        NewRouter.startRouter();
        System.out.println("started");
        this.toSend = new Stack<>();
        Route GETResource = (Request request, Response response) -> {
            response.type("application/json");
            System.out.println("sending");
            try {
                return this.toSend.pop();
            } catch (EmptyStackException e) {
                return "";
            }
        };
        Spark.get(path,GETResource);
    }

    @Override
    public void out(String response) throws IllegalArgumentException {
        String[] command = response.split(" ",3);
        if (command[0].equals("toGet")) {
            try {
                switch (command[1]) {
                    case "resource":
                        System.out.println("Sending");
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

    private void addResource(String jsonBody) {
        this.toSend.push(jsonBody);
        System.out.println(jsonBody);
        System.out.println("added to stack");
    }


}
