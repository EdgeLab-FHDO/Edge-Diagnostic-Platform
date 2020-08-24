package InfrastructureManager;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
TODO: Configurator should be able to return to the master a list of runners
    each one of them, has input (only one) and a list of outputs. Configurator
    should only do things that are needed in the start of the programm, for
    run time operations is the master.
 */

/**
 * Configurator class for the master, that takes the values in the configuration file
 * object and gives the master the different elements based on that (Different Input types,
 * output types, etc.)
 */
public class MasterConfigurator {
    private final String CONFIG_FILE_PATH = "src/main/resources/Configuration.json";
    private MasterConfigurationData data; //Configuration File Interface
    //TODO: consider making it a singleton

    public MasterConfigurator() {
        ObjectMapper mapper = new ObjectMapper(); //Using Jackson Functionality
        try {
            //Map the contents of the JSON file to a java object
            this.data = mapper.readValue(new File(CONFIG_FILE_PATH), MasterConfigurationData.class);
        } catch (IOException e) {
            System.out.println("Error while reading JSON Config File");
            e.printStackTrace();
        }
    }

    /**
     * Based on the commands defined in the config file, configures the CommandSet instance and returns it
     * @return The command set for the master to use
     */
    public CommandSet getCommands() {
        CommandSet.getInstance().set(data.getCommands());
        return CommandSet.getInstance();
    }

    /**
     * Based on the input defined in the config file, returns different types of input to the master
     * @return an object that implements the MasterInput interface
     */

    private MasterInput getInput(String inputString) {
        //TODO: Add more inputs
        switch (inputString) {
            case "console":
                return new ConsoleInput();
            /*case "dummyScenario" :
                ScenarioEditor editor = new ScenarioEditor();
                editor.scenarioFromFile("src/main/resources/scenarios/dummyScenario.json");
                return editor.getScenario();*/
            default:
                throw new IllegalArgumentException("Invalid input in Configuration");
        }
    }

    /**
     * Based on the output defined in the config file, returns different types of output objects to the master
     * @return an Object that implements the MasterOutput interface
     */

    private MasterOutput[] getOutputs(String[] outputStringArray) {
        //TODO: Add more outputs
        MasterOutput[] result = new MasterOutput[outputStringArray.length];
        for (int i = 0; i < outputStringArray.length; i++) {
            switch (outputStringArray[i]) {
                case "console":
                    result[i] = new ConsoleOutput();
                    break;
                case "util" :
                    result[i] = new MasterUtility();
                    break;
                case "scenario dispatcher" :
                    result[i] = new ScenarioDispatcher();
                    break;
                case  "scenario editor" :
                    result[i] = new ScenarioEditor();
                    break;
                default:
                    throw new IllegalArgumentException("Invalid output in Configuration");
            }
        }
        return result;
    }

    public ArrayList<Runner> getRunners(){
        ArrayList<Runner> result = new ArrayList<>();
        String input;
        MasterOutput[] output;
        String name;
        for (RunnerConfigData runnerData : data.getRunners()){
            input = runnerData.getInput();
            output = getOutputs(runnerData.getOutputs());
            name = runnerData.getName();
            if (runnerData.isScenario()) { //Input as scenario name if is an scenario runner
                result.add(new ScenarioRunner(name,input,output));
            } else { //Input as masterInput
                result.add(new Runner(name,getInput(input),output));
            }
        }
        return result;
    }

}
