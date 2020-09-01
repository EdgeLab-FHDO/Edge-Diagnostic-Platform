package InfrastructureManager.Rest;

import InfrastructureManager.MasterInput;
import spark.*;

public class RestInput implements MasterInput {
    private static String command = "";
    public static Route readCommand = (Request request, Response response) ->{
        command = request.params(":command");
        return command;
    };

    @Override
    public String read() {
        return command;
    }
}
