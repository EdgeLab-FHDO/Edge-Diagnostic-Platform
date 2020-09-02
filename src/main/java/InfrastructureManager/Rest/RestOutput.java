package InfrastructureManager.Rest;

import InfrastructureManager.MasterOutput;
import spark.*;

public class RestOutput implements MasterOutput {
    private static String output = "";

    public static Route printResponse = (Request request, Response response) ->{
        return request.params(":response");
    };
    @Override
    public void out(String response) {
        output = response;
    }
}