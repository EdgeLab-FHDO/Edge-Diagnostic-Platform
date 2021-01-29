package InfrastructureManager.Modules.Scenario;

import InfrastructureManager.ModuleManagement.ModuleOutput;
import InfrastructureManager.ModuleManagement.PlatformModule;
import InfrastructureManager.Modules.Scenario.Exception.Output.EmptyEventListException;
import InfrastructureManager.Modules.Scenario.Exception.Output.ScenarioEditorException;
import InfrastructureManager.Modules.Scenario.Exception.Output.ScenarioIOException;
import com.fasterxml.jackson.databind.InjectableValues;
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
public class ScenarioEditor extends ModuleOutput {

    private Scenario scenario;
    private final ObjectMapper mapper;

    public ScenarioEditor(PlatformModule module, String name) {
        super(module,name);
        InjectableValues inject = new InjectableValues.Std()
                .addValue(PlatformModule.class, module);
        //When saving to a file, make so the JSON string is indented and "pretty"
        this.mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        this.mapper.setInjectableValues(inject);
    }

    /**
     * Based on responses from the master executes the different functionalities
     * @param response Must be in the way "editor command" and additionally:
     *                 - Create Scenario : Add the name of the scenario (editor create scenarioName)
     *                 - Add Event : Add the Command for the event and the execution time (editor addEvent event1 1000)
     *                 - Delete : Just the command. (editor deleteEvent)
     *                 - Save to file : Add the path of the folder in which the file will be saved (editor toFile src/resources/scenarios/)
     *                 - Load from File: Add the path to the file (editor fromFile src/resources/scenario.json)
     *@throws ScenarioEditorException If the command is not defined or is missing arguments, or if one of the
     * internal functions throws either a {@link ScenarioIOException} or {@link EmptyEventListException}
     */
    @Override
    public void execute(String response) throws ScenarioEditorException {
        String[] command = response.split(" ");
        if (command[0].equals("editor")) {
            try {
                switch (command[1]) {
                    case "create" -> create();
                    case "addEvent" -> addEvent(command[2], Integer.parseInt(command[3]));
                    case "deleteEvent" -> deleteLastEvent();
                    case "toFile" -> scenarioToFile(command[2]);
                    case "fromFile" -> scenarioFromFile(command[2]);
                    default -> throw new ScenarioEditorException("Invalid command " + command[1]
                            + " for ScenarioEditor");
                }
            } catch (IndexOutOfBoundsException e){
                throw new ScenarioEditorException("Arguments missing for command " + response
                        + " to ScenarioEditor");
            }
        }
    }

    /**
     * Create a new scenario (related to this editor)
     */
    private void create(){
        scenario = new Scenario(this.getOwnerModule());
    }

    /**
     * Add an event to an already loaded (from file or created) scenario
     * @param command Command of the event
     * @param executionTime Relative execution time of the event (in ms)
     */
    private void addEvent(String command, int executionTime){
        scenario.addEvent(new Event(command,executionTime));
    }

    /**
     * Delete the last event from a loaded scenario (from file or created)
     */
    private void deleteLastEvent() throws EmptyEventListException {
        if (scenario.getEventList().isEmpty()) {
            throw new EmptyEventListException("Event list is empty!");
        } else {
            int last = scenario.getEventList().size() - 1;
            scenario.deleteEvent(last);
        }
    }

    /**
     * Save the loaded scenario (from file or created) to a JSON file
     * @param path Path of the folder to save the file (Filename is automatic)
     */
    private void scenarioToFile(String path) throws ScenarioIOException {
        String processedName = scenario.getName().substring(0, scenario.getName().indexOf('.'));
        path = path + processedName + ".json"; //Filename according to scenario name
        try {
            mapper.writeValue(new File(path), this.scenario);
        } catch (IOException e) {
            throw new ScenarioIOException("Scenario " + scenario.getName() + " could not be saved", e);
        }
    }

    /**
     * Load scenario from a JSON file
     * @param path Path of the file
     */
    private void scenarioFromFile(String path) throws ScenarioIOException {
        try {
            this.scenario = mapper.readValue(new File(path),Scenario.class);
        } catch (IOException e) {
            throw new ScenarioIOException("Scenario could not be loaded from path " + path, e);
        }
    }

    /**
     * Get the scenario being edited
     * @return Scenario being edited in the editor
     */
    public Scenario getScenario() { //FOR TESTS
        return scenario;
    }
}
