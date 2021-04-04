package InfrastructureManager.Modules.Scenario;

import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;
import InfrastructureManager.ModuleManagement.PlatformOutput;
import InfrastructureManager.Modules.Scenario.Exception.Output.EmptyEventListException;
import InfrastructureManager.Modules.Scenario.Exception.Output.ScenarioEditorException;
import InfrastructureManager.Modules.Scenario.Exception.Output.ScenarioIOException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;

/**
 * Class that represent Scenario editing as a platform output.
 *
 * This type of output is used for modifying the Scenario inside an ScenarioModule.
 *
 * It provides the following functionalities:
 *
 * - Add and delete Events from a Scenario
 * - Saving a Scenario to a JSON file
 */
public class ScenarioEditor extends ScenarioModuleObject implements PlatformOutput {

    private final Scenario scenario;
    private final ObjectMapper mapper;

    /**
     * Constructor of the class. Creates a new Scenario editor.
     *
     * @param module Owner module of this output
     * @param name   Name of this output. Normally hardcoded "MODULE_NAME.editor"
     */
    public ScenarioEditor(ImmutablePlatformModule module, String name) {
        super(module,name);
        this.scenario = this.getScenario();
        //When saving to a file, make so the JSON string is indented and "pretty"
        this.mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
    }

    /**
     * Based on processed responses from the inputs executes the different functionalities
     *
     * @param response Must be in the way "editor COMMAND" and additionally:
     *                 - Add Event : Add the Command for the event and the execution time (editor addEvent event1 1000)
     *                 - Delete : Just the command. (editor deleteEvent)
     *                 - Save to file : Add the path of the folder in which the file will be saved (editor toFile src/resources/scenarios/)
     * @throws ScenarioEditorException If the command is not defined or is missing arguments, or if one of the internal functions throws either a {@link ScenarioIOException} or {@link EmptyEventListException}
     */
    @Override
    public void execute(String response) throws ScenarioEditorException {
        this.getLogger().debug(this.getName() + " - Scenario execute");
        String[] command = response.split(" ");
        if (command[0].equals("editor")) {
            this.getLogger().debug(this.getName() + " Editor cmd additional cmd: "+ command[1]);
            try {
                switch (command[1]) {
                    case "addEvent" -> addEvent(command[2], Integer.parseInt(command[3]));
                    case "deleteEvent" -> deleteLastEvent();
                    case "toFile" -> scenarioToFile(command[2]);
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
     * Add an event to a scenario-
     *
     * @param command       Command of the event
     * @param executionTime Relative execution time of the event (in ms)
     */
    private void addEvent(String command, int executionTime){
        this.getLogger().debug(this.getName() + " - Add event command: "+ command+ "execution time: "+executionTime);
        scenario.addEvent(new Event(command,executionTime));
    }

    /**
     * Delete the last event from a Scenario
     *
     * @throws EmptyEventListException If the scenario has no events to delete
     */
    private void deleteLastEvent() throws EmptyEventListException {
        this.getLogger().debug(this.getName()+" - Deleting last event");
        if (scenario.getEventList().isEmpty()) {
            throw new EmptyEventListException("Event list is empty!");
        } else {
            int last = scenario.getEventList().size() - 1;
            scenario.deleteEvent(last);
        }
    }

    /**
     * Save a scenario to a JSON file.
     *
     * The file will be automatically named as "MODULE_NAME.json" according to the owner module's name.
     *
     * @param path Path of the folder to save the file (Filename is automatic)
     * @throws ScenarioIOException If an error happens while writing to the file.
     */
    private void scenarioToFile(String path) throws ScenarioIOException {
        this.getLogger().debug(this.getName() + " - Saving scenario to Json file");
        String processedName = scenario.getName().substring(0, scenario.getName().indexOf('.'));
        path = path + processedName + ".json"; //Filename according to scenario name
        try {
            mapper.writeValue(new File(path), this.scenario);
        } catch (IOException e) {
            throw new ScenarioIOException("Scenario " + scenario.getName() + " could not be saved", e);
        }
    }
}
