package InfrastructureManager.Rest;

import InfrastructureManager.Master;
import InfrastructureManager.MasterInput;
import spark.*;

public class RestInput implements MasterInput {
    private static String command = "";
    private static Master master = Master.getInstance();

    public static Route readCommandTest = (Request request, Response response) -> request.params(":command");

    public static Route executeCommandTest = (Request request, Response response) ->{
        String executed = request.params(":command");
        return master.execute(executed);
    };

    public static Route executeCommand = (Request request, Response response) ->{
        command = request.params(":command");
        return command;
    };

    @Override
    public String read() throws Exception {
        if(command.replaceAll("\\s+","").isEmpty()) {
           throw new Exception("No command exception");
        }
        String executed = command;
        command = "";
        return executed;
    }
}
