package InfrastructureManager.Modules.RemoteExecution.Output;

import InfrastructureManager.ModuleManagement.ModuleOutput;
import InfrastructureManager.ModuleManagement.PlatformModule;
import InfrastructureManager.Modules.RemoteExecution.Exception.NodeLimit.InvalidLimitParametersException;
import InfrastructureManager.Modules.RemoteExecution.Exception.NodeLimit.NodeLimitException;
import InfrastructureManager.Modules.RemoteExecution.LimitList;

public class NodeLimitOutput extends ModuleOutput {

    private final LimitList sharedList;

    public NodeLimitOutput(PlatformModule module, String name, LimitList list) {
        super(module,name);
        this.sharedList = list;
    }

    @Override
    public void execute(String response) throws NodeLimitException {
        String[] command = response.split(" ");
        if (command[0].equals("limit")) {
            try {
                switch (command[1]) {
                    case "cores" -> {
                        String period = command.length > 4 ? command[4] : "100000";
                        addToLimitList(command[2], command[3], period);
                    }
                    default -> throw new NodeLimitException("Invalid command " + command[1] + " for NodeLimiter");
                }
            } catch (IndexOutOfBoundsException e) {
                throw new NodeLimitException("Arguments missing for command " + response  + " to NodeLimiter");
            }
        }
    }

    private void addToLimitList(String tag, String limit, String period_ms) throws InvalidLimitParametersException {
        try {
            int quota_ms = (int) (Double.parseDouble(limit) * Integer.parseInt(period_ms));
            this.sharedList.putValue(tag, quota_ms + "_" + period_ms);
        } catch (NumberFormatException | NullPointerException e) {
            throw new InvalidLimitParametersException("Parameters to set limits were invalid", e);
        }
    }
}
