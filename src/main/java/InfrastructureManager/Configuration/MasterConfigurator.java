package InfrastructureManager.Configuration;

import InfrastructureManager.AdvantEdge.AdvantEdgeClient;
import InfrastructureManager.Configuration.RawData.ConnectionConfigData;
import InfrastructureManager.Configuration.RawData.IOConfigData;
import InfrastructureManager.Configuration.RawData.MasterConfigurationData;
import InfrastructureManager.*;
import InfrastructureManager.MatchMaking.MatchMaker;
import InfrastructureManager.Rest.RestInput;
import InfrastructureManager.Rest.RestOutput;
import InfrastructureManager.Rest.RestRunner;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Configurator class for the master, that takes the values in the configuration file
 * object and gives the master the different elements based on that (Commands, and Runners)
 */
public class MasterConfigurator {

    private final String CONFIG_FILE_PATH = "src/main/resources/Configuration3.json";
    private MasterConfigurationData data; //Configuration File Interface
    private Map<String,MasterInput> inputInstances;
    private List<String> scenarios;
    private Map<String,MasterOutput> outputInstances;
    private boolean activateRestRunner = false;
    //TODO: consider making it a singleton


    public MasterConfigurator() {
        ObjectMapper mapper = new ObjectMapper(); //Using Jackson Functionality
        try {
            //Map the contents of the JSON file to a java object
            this.data = mapper.readValue(new File(CONFIG_FILE_PATH), MasterConfigurationData.class);
            this.inputInstances = new HashMap<>();
            this.outputInstances = new HashMap<>();
            this.scenarios = new ArrayList<>();
        } catch (IOException e) {
            System.out.println("Error while reading JSON Config File");
            e.printStackTrace();
        }
    }

    /**
     * Extracts data from the configuration file to assign different input instances to their names
     */
    private void fillInputInstances() {
        MasterInput input;
        for (IOConfigData inputData : this.data.getIoData().getInputs()) {
            if (inputData.getType().equals("Scenario")) {
                this.scenarios.add(inputData.getName());
            } else {
                if (this.outputInstances.containsKey(inputData.getName())) {
                    input = (MasterInput) this.outputInstances.get(inputData.getName());
                } else {
                    input = this.getInputFromData(inputData);
                }
                this.inputInstances.put(inputData.getName(), input);
            }
        }
    }

    /**
     * Extracts output data from the configuration file, including ports
     */
    private void fillOutputInstances() {
        MasterOutput output;
        for (IOConfigData outputData : this.data.getIoData().getOutputs()) {
            if (this.inputInstances.containsKey(outputData.getName())) { //If an instance is input and output
                output = (MasterOutput) this.inputInstances.get(outputData.getName());
            } else {
                output = getOutputFromData(outputData);
            }
            this.outputInstances.put(outputData.getName(), output);
        }
    }

    private MasterOutput getOutputFromData(IOConfigData outputData) throws IllegalArgumentException {
        int port = outputData.getPort();
        switch (outputData.getType()) {
            case "ConsoleOutput":
                return new ConsoleOutput(outputData.getName());
            case "MasterUtility" :
                return new MasterUtility(outputData.getName());
            case "ScenarioDispatcher" :
                return new ScenarioDispatcher(outputData.getName());
            case  "ScenarioEditor" :
                return new ScenarioEditor(outputData.getName());
            case "RestOutput":
                return RestOutput.getInstance(outputData.getName());
            case "MatchMaker" :
                return new MatchMaker(outputData.getName());
            case "AdvantEdgeClient" :
                return new AdvantEdgeClient(outputData.getName(),port != 0 ? port : 80);
            default:
                throw new IllegalArgumentException("Invalid output in Configuration");
        }
    }

    private MasterInput getInputFromData(IOConfigData inputData) throws IllegalArgumentException {
        int port = inputData.getPort();
        switch (inputData.getType()) {
            case "ConsoleInput":
                return new ConsoleInput();
            case "MatchMaker":
                return new MatchMaker(inputData.getName());
            case "RestInput":
                RestRunner.getRestRunner("RestServer",port != 0 ? port : 4567);
                activateRestRunner = true;
                return new RestInput();
            default:
                throw new IllegalArgumentException("Invalid input in Configuration");
        }
    }

    private MasterInput getInput(String inputName) {
        return this.inputInstances.get(inputName);
    }

    private MasterOutput[] getOutputs(String inputName) {
        List<MasterOutput> result = new ArrayList<>();
        for (ConnectionConfigData configData : this.data.getConnections()) {
            if (configData.getIn().equals(inputName)) {
                result.add(this.outputInstances.get(configData.getOut()));
            }
        }
        return result.toArray(new MasterOutput[0]);
    }

    private Map<String, CommandSet> getCommands(String inputName) {
        Map<String, CommandSet> result = new HashMap<>();
        CommandSet commands;
        for (ConnectionConfigData configData : this.data.getConnections()) {
            if (configData.getIn().equals(inputName)) {
                commands = new CommandSet();
                commands.set(configData.getCommands());
                result.put(configData.getOut(),commands);
            }
        }
        return result;
    }


    /**
     * Based on the configuration file, returns the runners that the master should use
     * @return ArrayList of Runner objects for the master to run
     */
    public List<Runner> getRunners(){
        List<Runner> result = new ArrayList<>();
        MasterInput runnerInput;
        MasterOutput[] runnerOutputs;
        String runnerName = "Runner_";
        fillInputInstances();
        fillOutputInstances();
        for (String inputName : this.data.getConnectedInputs()) {
            runnerOutputs = getOutputs(inputName);
            if (this.scenarios.contains(inputName)) {
                ScenarioRunner scenarioRunner = new ScenarioRunner(runnerName + inputName, inputName, runnerOutputs);
                scenarioRunner.setConfiguredCommands(getCommands(inputName));
                result.add(scenarioRunner);
            } else {
                runnerInput = getInput(inputName);
                Runner runner = new Runner(runnerName + inputName, runnerInput, runnerOutputs);
                runner.setConfiguredCommands(getCommands(inputName));
                result.add(runner);
            }
        }
        if (activateRestRunner) {
            try {
                result.add(RestRunner.getRestRunner());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

}
