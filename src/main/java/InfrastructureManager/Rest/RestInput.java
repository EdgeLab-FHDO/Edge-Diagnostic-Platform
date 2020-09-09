package InfrastructureManager.Rest;

import InfrastructureManager.Master;
import InfrastructureManager.MasterInput;
import spark.*;

public class RestInput implements MasterInput {
    private static String command = "";
    private static Master master = Master.getInstance();

    public static Route readCommand = (Request request, Response response) ->{
        command = request.params(":command");
        return command;
    };

    public static Route executeCommand = (Request request, Response response) ->{
        command = request.params(":command");
        return master.execute(command);
    };

    @Override
    public String read() throws Exception {
        if(command.isBlank()) {
           throw new Exception("No command exception");
        }
        return command;
    }
}
