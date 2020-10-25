package InfrastructureManager.Configuration;

import InfrastructureManager.*;
import InfrastructureManager.AdvantEdge.AdvantEdgeClient;
import InfrastructureManager.Configuration.RawData.IOConfigData;
import InfrastructureManager.Configuration.RawData.IOPortConfigData;
import InfrastructureManager.Configuration.RawData.MasterConfigurationData;
import InfrastructureManager.Configuration.RawData.RunnerConfigData;
import InfrastructureManager.MatchMaking.MatchMaker;
import InfrastructureManager.Rest.RestInput;
import InfrastructureManager.Rest.RestOutput;
import InfrastructureManager.Rest.RestRunner;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Configurator class for the master, that takes the values in the configuration file
 * object and gives the master the different elements based on that (Commands, and Runners)
 */
public class MasterConfigurator {

    private final String CONFIG_FILE_PATH = "src/main/resources/Configuration.json";
    private MasterConfigurationData data; //Configuration File Interface
    private Map<String,MasterInput> inputInstances;
    private Map<String,MasterOutput> outputInstances;
    //TODO: consider making it a singleton

    public MasterConfigurator() {
        ObjectMapper mapper = new ObjectMapper(); //Using Jackson Functionality
        try {
            //Map the contents of the JSON file to a java object
            this.data = mapper.readValue(new File(CONFIG_FILE_PATH), MasterConfigurationData.class);
            this.inputInstances = new HashMap<>();
            this.outputInstances = new HashMap<>();
            //this.data.getIoData().printInfo();
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
     * Extracts data from the configuration file to assign different input instances to their names
     */
    private void fillInputInstances() {
        for (IOConfigData inputData : this.data.getIoData().getInputs()) {
            this.inputInstances.put(inputData.getName(), getInputFromType(inputData.getType()));
        }
    }

    /**
     * Extracts output data from the configuration file, including ports
     */
    private void fillOutputInstances() {
        for (IOConfigData outputData : this.data.getIoData().getOutputs()) {
            if (this.inputInstances.containsKey(outputData.getName())) { //If an instance is input and output
                this.outputInstances.put(outputData.getName(), (MasterOutput) this.inputInstances.get(outputData.getName()));
            } else {
                MasterOutput output;
                Integer port = getPort(outputData);
                if (port != null) {
                    output = getPortOutputFromType(outputData.getType(),port);
                } else {
                    output = getOutputFromType(outputData.getType());
                }
                this.outputInstances.put(outputData.getName(),output);
            }
        }
    }

    private Integer getPort(IOConfigData ioData) {
        for (IOPortConfigData portData : this.data.getIoData().getPorts()) {
            if (portData.getName().equals(ioData.getName())) {
                return portData.getPort();
            }
        }
        return null;
    }

    private MasterOutput getOutputFromType(String outputStringType) throws IllegalArgumentException {
        switch (outputStringType) {
            case "ConsoleOutput":
                return new ConsoleOutput();
            case "MasterUtility" :
                return new MasterUtility();
            case "ScenarioDispatcher" :
                return new ScenarioDispatcher();
            case  "ScenarioEditor" :
                return new ScenarioEditor();
            case "RestOutput":
                return RestOutput.getInstance();
            case "MatchMaker" :
                return new MatchMaker();
            default:
                throw new IllegalArgumentException("Invalid output in Configuration");
        }
    }

    private MasterOutput getPortOutputFromType(String outputStringType, int portNumber) throws IllegalArgumentException {
        switch (outputStringType) {
            case "AdvantEdgeClient" :
                return new AdvantEdgeClient(portNumber);
            default:
                throw new IllegalArgumentException("Invalid output in Configuration");
        }
    }


    /**
     * Based on the input defined in the config file, returns different instances of masterInputs
     * @return an object that implements the MasterInput interface
     * @throws IllegalArgumentException if input string in the configuration is not defined
     */
    private MasterInput getInputFromType(String inputStringType) throws IllegalArgumentException {
        switch (inputStringType) {
            case "ConsoleInput":
                return new ConsoleInput();
            case "RestInput":
                return new RestInput();
            case "MatchMaker":
                return new MatchMaker();
            default:
                throw new IllegalArgumentException("Invalid input in Configuration");
        }
    }

    /**
     * According to the input instance name, returns the instance.
     * @param inputName Name of the input instance, declared in ioData in config file
     * @return MasterInput instance corresponding to the input name
     */
    private MasterInput getInput(String inputName) {
        if (this.inputInstances.isEmpty()) {
            fillInputInstances();
        }
        return this.inputInstances.get(inputName);
    }

    /**
     * According to their defined names, returns the output instances to be used by the runners
     * @param outputNames Names of the outputs, defined in ioData in config file
     * @return Output instances as an array
     */
    private MasterOutput[] getOutputs(String[] outputNames) {
        MasterOutput[] result = new MasterOutput[outputNames.length];
        if (this.outputInstances.isEmpty()) {
            fillOutputInstances();
        }
        for (int i = 0; i < result.length; i++) {
            result[i] = this.outputInstances.get(outputNames[i]);
        }
        return result;
    }


    /**
     * Based on the configuration file, returns the runners that the master should use
     * @return ArrayList of Runner objects for the master to run
     */
    public ArrayList<Runner> getRunners(){
        ArrayList<Runner> result = new ArrayList<>();
        String name;
        String input;
        for (RunnerConfigData runnerData : this.data.getRunners()) {
            name = runnerData.getName();
            input = runnerData.getInput();
            if (runnerData.isScenario()) { //Input as scenario name if is an scenario runner
                result.add(new ScenarioRunner(name,input,getOutputs(runnerData.getOutputs())));
            } else if (name.equals("RestServer")) {
                String portNumber = input.replaceAll("[^\\d]*","");
                result.add(RestRunner.getRestRunner(name, Integer.parseInt(portNumber)));
            } else { //Input as masterInput
                result.add(new Runner(name, getInput(input),getOutputs(runnerData.getOutputs())));
            }
        }
        return result;
    }

}
