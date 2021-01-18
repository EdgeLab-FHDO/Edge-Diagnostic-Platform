package InfrastructureManager.Configuration;


import InfrastructureManager.Configuration.Exception.ResponseNotDefinedException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Singleton Class that interfaces the command set to the master
 * Implements the MasterConfig and MasterResource generic interfaces with a Map of Strings
 */
public class CommandSet {

    private Map<String,String> commands;
    private final Map<String,List<Integer>> parameterMapping;

    public CommandSet() {
        this.commands = new HashMap<>();
        this.parameterMapping = new HashMap<>();
    }

    /**
     * Set the command set to a given map
     * @param config Map<String,String> element containing the commands in the form of ({command} : {response})
     */
    public void set(Map<String, String> config) {
        this.commands = config;
        fillMapping();
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
    public String getResponse(String command) throws IllegalArgumentException, ResponseNotDefinedException {
        String[] commandAsArray = command.split(" ");
        command = commandAsArray[0];
        if (command.isEmpty()) {
            throw new IllegalArgumentException("Empty Command at input!");
        } else {
            String response = getUnmappedResponse(commandAsArray);
            return mapCommand(commandAsArray,response);
        }
    }

    private void fillMapping() {
        for (String command : this.commands.keySet()){
            this.parameterMapping.put(command.split(" ")[0],getMappingPositions(command));
        }
    }

    private List<Integer> getMappingPositions(String command) {
        List<Integer> result = new ArrayList<>();
        String[] commandAsArray = command.split(" ");
        String response = this.commands.get(command);
        for (int i = 1; i < commandAsArray.length; i++) {
            if (response.contains(commandAsArray[i])) {
                result.add(i);
            }
        }
        return result;
    }

    private String getUnmappedResponse(String[] commandAsArray) throws ResponseNotDefinedException {
        String[] aux;
        for (String definedCommand : this.commands.keySet()) {
            if (definedCommand.startsWith(commandAsArray[0])) {
                aux = definedCommand.split(" ");
                if (aux.length == commandAsArray.length){
                    return this.commands.get(definedCommand);
                }
            }
        }
        throw new ResponseNotDefinedException("Response could not be found for command " + commandAsArray[0]);
    }

    private String mapCommand(String[] commandAsArray, String response) {
        List<Integer> positions = this.parameterMapping.get(commandAsArray[0]);
        for (Integer position : positions) {
            response = response.replaceFirst("\\s+\\$.+?\\b", " " + commandAsArray[position]);
        }
        return response;
    }
}
