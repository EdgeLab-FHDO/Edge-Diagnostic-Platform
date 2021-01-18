package InfrastructureManager.Modules.RemoteExecution.Output;

import InfrastructureManager.ModuleManagement.ModuleOutput;
import InfrastructureManager.Modules.RemoteExecution.LimitList;

public class NodeLimitOutput extends ModuleOutput {

    private final LimitList sharedList;

    public NodeLimitOutput(String name, LimitList list) {
        super(name);
        this.sharedList = list;
    }

    @Override
    public void out(String response) throws IllegalArgumentException {
        String[] command = response.split(" ");
        if (command[0].equals("limit")) {
            try {
                switch (command[1]) {
                    case "cores" -> {
                        String period = command.length > 4 ? command[4] : "100000";
                        addToLimitList(command[2], command[3], period);
                    }
                    default -> throw new IllegalArgumentException("Invalid command for NodeLimiter");
                }
            } catch (IndexOutOfBoundsException e) {
                throw new IllegalArgumentException("Arguments missing for command - NodeLimiter");
            }
        }
    }

    private void addToLimitList(String tag, String limit, String period_ms) {
        int quota_ms = (int) (Double.parseDouble(limit) * Integer.parseInt(period_ms));
        this.sharedList.putValue(tag, quota_ms + "_" + period_ms);
    }
}
