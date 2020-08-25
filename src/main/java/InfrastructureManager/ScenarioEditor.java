package InfrastructureManager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;

/**
 * Scenario Handling class that is an Output of the master
 * Allows for handling scenarios in the following ways :
 * - Create Scenarios
 * - Add/ Delete Event to Scenarios
 * - Save Scenarios to JSON Files
 * - Load Scenarios from JSON Files
 */
public class ScenarioEditor implements MasterOutput{

    private Scenario scenario;
    private final ObjectMapper mapper;

    public ScenarioEditor() {
        //When saving to a file, make so the JSON string is indented and "pretty"
        this.mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
    }

    /**
     * Based on responses from the master executes the different functionalities
     * @param response Must be in the way "editor command" and additionally:
     *                 - Create Scenario : Add the name of the scenario (editor create scenarioName)
     *                 - Add Event : Add the Command for the event (editor addEvent event1)
     *                 - Delete : Just the command. (editor deleteEvent)
     *                 - Save to file : Add the path of the folder in which the file will be saved (editor toFile src/resources/scenarios/)
     *                 - Load from File: Add the path to the file (editor fromFile src/resources/scenario.json)
     *@throws IllegalArgumentException If the command is not defined or is missing arguments
     */
    @Override
    public void out(String response) {
        String[] command = response.split(" ");
        if (command[0].equals("editor")) {
            try {
                switch (command[1]) {
                    case "create":
                        create(command[2]);
                        break;
                    case "addEvent":
                        addEvent(command[2]);
                        break;
                    case "deleteEvent":
                        deleteLastEvent();
                        break;
                    case "toFile" :
                        scenarioToFile(command[2]);
                        break;
                    case "fromFile":
                        scenarioFromFile(command[2]);
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid command for ScenarioEditor");
                }
            } catch (IndexOutOfBoundsException e){
                throw new IllegalArgumentException("Arguments missing for command  - ScenarioEditor");
            }
        }
    }

    /**
     * Create a new scenario (related to this editor)
     * @param name Name of the scenario to be created
     */
    private void create(String name){
        scenario = new Scenario(name);
    }

    /**
     * Add an event to an already loaded (from file or created) scenario
     * @param command Command of the event
     */
    private void addEvent(String command){
        scenario.addEvent(new Event(command));
    }

    /**
     * Delete the last event from a loaded scenario (from file or created)
     */
    private void deleteLastEvent(){
        int last = scenario.getEventList().size() - 1;
        if (last >= 0) {
            scenario.deleteEvent(last);
        }
    }

    /**
     * Save the loaded scenario (from file or created) to a JSON file
     * @param path Path of the folder to save the file (Filename is automatic)
     */
    private void scenarioToFile(String path){
        path = path + scenario.getName() + ".json"; //Filename according to scenario name
        try {
            mapper.writeValue(new File(path), this.scenario);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Load scenario from a JSON file
     * @param path Path of the file
     */
    private void scenarioFromFile(String path){
        try {
            this.scenario = mapper.readValue(new File(path),Scenario.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
