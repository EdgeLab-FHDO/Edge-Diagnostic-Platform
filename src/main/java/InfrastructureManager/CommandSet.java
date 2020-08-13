package InfrastructureManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CommandSet implements MasterConfig<Map<String,String>> {

    private static CommandSet instance = null; //Singleton Implementation, only one command set will be necessary

    private Map<String,String> commands;
    private final String FILE_PATH = "src/main/resources/commands.txt";

    private CommandSet() {
        this.commands = new HashMap<>();
    }

    public static CommandSet getInstance() { //Singleton implementation
        if (instance == null) {
            instance = new CommandSet();
        }
        return instance;
    }

    @Override
    public void set(Map<String, String> config) {
        this.commands = config;
    }

    @Override
    public Map<String, String> get() {
        return this.commands;
    }

    public String getResponse(String command) {
        if (command.isEmpty()) {
            return "Empty Command!";
        } else {
            return this.commands.getOrDefault(command,"command not defined!");
        }
    }
}
