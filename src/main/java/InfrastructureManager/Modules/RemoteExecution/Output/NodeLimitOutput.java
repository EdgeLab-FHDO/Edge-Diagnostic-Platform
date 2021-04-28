package InfrastructureManager.Modules.RemoteExecution.Output;

import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;
import InfrastructureManager.ModuleManagement.PlatformOutput;
import InfrastructureManager.Modules.RemoteExecution.Exception.NodeLimit.InvalidLimitParametersException;
import InfrastructureManager.Modules.RemoteExecution.Exception.NodeLimit.NodeLimitException;
import InfrastructureManager.Modules.RemoteExecution.RemoteExecutionModuleObject;

/**
 * Class that represents creating limits to a node's CPU usage as an output of the platform
 * <p>
 * This type of output is used for creating the resource limits (CPU) that are eventually sent to the machine(s).
 * <p>
 * Stores the limit information into a {@link InfrastructureManager.Modules.RemoteExecution.LimitList} which is then read by a
 * {@link InfrastructureManager.Modules.RemoteExecution.Input.NodeLimitInput} instance in order to route the created limits to another
 * platform output.
 */
public class NodeLimitOutput extends RemoteExecutionModuleObject implements PlatformOutput {

    /**
     * Constructor of the class. Creates a new NodeLimitOutput.
     *
     * @param module Owner module of this output
     * @param name   Name of this output. Normally hardcoded "MODULE_NAME.limit.out"
     */
    public NodeLimitOutput(ImmutablePlatformModule module, String name) {
        super(module,name);
    }

    /**
     * Based on processed responses from the inputs executes the different functionalities
     * @param response Must be in a way "limit COMMAND" and additionally:
     *                 - To limit CPU usage: In addition to the "cores" command, should include the tag of the node to limit and the
     *                 CPU limit (expressed as a fraction of 1, for example 0.5).
     *                 Optionally, it can include a period argument to set the period (in microseconds) in which the tasks of the node to be limited will be executed. The quota is then calculated automatically having the period and cpu%.
     *                 This is an optional parameter, if not set, it will be set to 100000 by default
     * @throws NodeLimitException If the passed command was invalid or was missing arguments
     */
    @Override
    public void execute(String response) throws NodeLimitException {
        String[] command = response.split(" ");
        this.getLogger().debug(this.getName(), "Limit command execute method, resp: " + response);
        if (command[0].equals("limit")) {
            this.getLogger().debug(this.getName(), "Limit command additional cmd: " + command[1]);
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

    /**
     * Adds a limit to the shared {@link InfrastructureManager.Modules.RemoteExecution.LimitList}.
     *
     * @param tag       The tag of the node to be limited
     * @param limit     Limit of the CPU as a fraction of 1 ("0.5" for example)
     * @param period_ms  Period (in microseconds) in which the tasks of the node to be limited will be executed.
     * @throws InvalidLimitParametersException If the passed parameters were not valid. This includes null parameters and
     * invalid numeric strings for the limit and period parameters
     */
    private void addToLimitList(String tag, String limit, String period_ms) throws InvalidLimitParametersException {
        this.getLogger().debug(this.getName(), "Adding a limit to the shared Limit List, tag: " + tag + ", Limit: " + limit + ", period_ms: " + period_ms);
        try {
            int quota_ms = (int) (Double.parseDouble(limit) * Integer.parseInt(period_ms));
            this.getLimitList().putValue(tag, quota_ms + "_" + period_ms);
        } catch (NumberFormatException | NullPointerException e) {
            throw new InvalidLimitParametersException("Parameters to set limits were invalid", e);
        }
    }
}
