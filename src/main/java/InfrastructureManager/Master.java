package InfrastructureManager;

import java.util.HashMap;
import java.util.Map;

public class Master {
    private CommandSet commandSet;

    public Master() {
        commandSet = new CommandSet();
    }

    public String execute(String command) {
        return this.commandSet.getCommand(command);
    }

}
