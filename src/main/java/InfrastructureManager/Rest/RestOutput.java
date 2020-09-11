package InfrastructureManager.Rest;

import InfrastructureManager.Master;
import InfrastructureManager.MasterOutput;

public class RestOutput implements MasterOutput {
    private static String output = "";

    public static String printResponse() {
        String response = output;
        output = "";
        return response;
    };

    @Override
    public void out(String response) {
        String[] command = response.split(" ");
        if (command[0].equals("rest")) {
            try {
                output = command[1];
            } catch (IndexOutOfBoundsException e) {
                throw new IllegalArgumentException("Arguments missing for command - RESTOutput");
            }
        }
    }
}