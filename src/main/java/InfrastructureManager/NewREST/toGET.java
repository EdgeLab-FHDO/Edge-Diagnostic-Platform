package InfrastructureManager.NewREST;

import InfrastructureManager.MasterOutput;

import java.util.Stack;

public class toGET extends MasterOutput {

    private Stack<String> toSend;

    public toGET(String name) {
        super(name);
        toSend = new Stack<>();
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

    private void addResource(String jsonBody) {

    }
}
