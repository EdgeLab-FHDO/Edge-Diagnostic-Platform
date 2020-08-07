package InfrastructureManager;

public class Master {
    private final CommandSet commandSet;

    public Master() {
        commandSet = new CommandSet();
    }

    public String execute(String command) {
        return this.commandSet.getResponse(command);
    }

}
