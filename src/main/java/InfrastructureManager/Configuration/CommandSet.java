package InfrastructureManager.Configuration;


import java.util.HashMap;
import java.util.Map;

/**
 * Singleton Class that interfaces the command set to the master
 * Implements the MasterConfig and MasterResource generic interfaces with a Map of Strings
 */
public class CommandSet {

    private Map<String,String> commands;

    public CommandSet() {
        this.commands = new HashMap<>();
    }

    /**
     * Set the command set to a given map
     * @param config Map<String,String> element containing the commands in the form of ({command} : {response})
     */
    public void set(Map<String, String> config) {
        this.commands = config;
    }

    /**
     * Return the commands in form of a map
     * @return Map<String,String> element containing the commands in the form of ({command} : {response})
     */
    public Map<String, String> get() {
        return this.commands;
    }

    /**
     * Mapping method to get a certain defined response for a given command
     * @param command Command for which the response is wanted
     * @return Response defined for the given command or "command not defined!" if the
     * given command is not configured
     * @throws IllegalArgumentException if the command is not defined or is empty
     */
    public String getResponse(String command) throws IllegalArgumentException {
        String[] aux = command.split(" ");
        String param = command.replace(aux[0],"");
        command = aux[0];
        if (command.isEmpty()) {
            throw new IllegalArgumentException("Empty Command at input!");
        } else {
            if (this.commands.containsKey(command)) {
                return this.commands.get(command) + param;
            } else {
                return null;
            }
        }
    }
}
