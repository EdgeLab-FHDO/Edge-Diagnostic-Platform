package InfrastructureManager.Configuration;

import InfrastructureManager.*;
import InfrastructureManager.AdvantEdge.AdvantEdgeClient;
import InfrastructureManager.Configuration.RawData.ConnectionConfigData;
import InfrastructureManager.Configuration.RawData.IOConfigData;
import InfrastructureManager.Configuration.RawData.MasterConfigurationData;
import InfrastructureManager.Configuration.RawData.RunnerConfigData;
import InfrastructureManager.MatchMaking.MatchMaker;
import InfrastructureManager.Rest.RestInput;
import InfrastructureManager.Rest.RestOutput;
import InfrastructureManager.Rest.RestRunner;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
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

    //TODO: Command Handling
    public CommandSet getCommands() {
        Map<String,String> oe = new HashMap<>();
        oe.put("deploy_application","console helmChartExecution");
        oe.put("update_GUI","console updateGUIExecution");
        CommandSet.getInstance().set(oe);
        return CommandSet.getInstance();
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
                    if (inputData.getPort() != 0) {
                        input = getInputFromType(inputData.getType(),inputData.getPort());
                    } else {
                        input = getInputFromType(inputData.getType());
                    }
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
                if (outputData.getPort() != 0) {
                    output = getOutputFromType(outputData.getType(),outputData.getPort());
                } else {
                    output = getOutputFromType(outputData.getType());
                }
            }
            this.outputInstances.put(outputData.getName(), output);
        }
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

    private MasterOutput getOutputFromType(String outputStringType, int portNumber) throws IllegalArgumentException {
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
            case "MatchMaker":
                return new MatchMaker();
            default:
                throw new IllegalArgumentException("Invalid input in Configuration");
        }
    }

    private MasterInput getInputFromType(String outputStringType, int portNumber) throws IllegalArgumentException {
        switch (outputStringType) {
            case "RestInput":
                RestRunner.getRestRunner("RestServer",portNumber);
                activateRestRunner = true;
                return new RestInput();
            default:
                throw new IllegalArgumentException("Invalid output in Configuration");
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


    /**
     * Based on the configuration file, returns the runners that the master should use
     * @return ArrayList of Runner objects for the master to run
     */
    public List<Runner> getRunners(){
        List<Runner> result = new ArrayList<>();
        MasterInput runnerInput;
        MasterOutput[] runnerOutputs;
        String runnerName = "Runner ";
        fillInputInstances();
        fillOutputInstances();
        for (String inputName : this.data.getConnectedInputs()) {
            runnerOutputs = getOutputs(inputName);
            if (this.scenarios.contains(inputName)) {
                result.add(new ScenarioRunner(runnerName + inputName, inputName, runnerOutputs));
            } else {
                runnerInput = getInput(inputName);
                result.add(new Runner(runnerName + inputName, runnerInput, runnerOutputs));
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
