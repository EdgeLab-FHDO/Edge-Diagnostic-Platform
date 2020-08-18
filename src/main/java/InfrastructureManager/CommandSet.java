package InfrastructureManager;


import java.util.HashMap;
import java.util.Map;

/**
 * Singleton Class that interfaces the command set to the master
 * Implements the MasterConfig and MasterResource generic interfaces with a Map of Strings
 */
public class CommandSet implements MasterConfig<Map<String,String>> {

    private static CommandSet instance = null; //Singleton Implementation, only one command set will be necessary

    private Map<String,String> commands;

    private CommandSet() {
        this.commands = new HashMap<>();
    }

    public static CommandSet getInstance() { //Singleton implementation
        if (instance == null) {
            instance = new CommandSet();
        }
        return instance;
    }

    /**
     * Set the command set to a given map
     * @param config Map<String,String> element containing the commands in the form of ({command} : {response})
     */
    @Override
    public void set(Map<String, String> config) {
        this.commands = config;
    }

    /**
     * Return the commands in form of a map
     * @return Map<String,String> element containing the commands in the form of ({command} : {response})
     */
    @Override
    public Map<String, String> get() {
        return this.commands;
    }

    /**
     * Mapping method to get a certain defined response for a given command
     * @param command Command for which the response is wanted
     * @return Response defined for the given command or "command not defined!" if the
     * given command is not configured
     */
    public String getResponse(String command) {
        String[] aux = command.split(" ");
        String param = command.replace(aux[0],"");
        command = aux[0];
        if (command.isEmpty()) {
            return "Empty Command!";
        } else {
            return this.commands.getOrDefault(command,"command not defined!") + param;
        }
    }
}
