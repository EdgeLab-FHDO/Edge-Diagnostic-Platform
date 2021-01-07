package InfrastructureManager.Configuration;

import InfrastructureManager.AdvantEdge.AdvantEdgeClient;
import InfrastructureManager.Configuration.RawData.*;
import InfrastructureManager.*;
import InfrastructureManager.FileOutput.FileOutput;
import InfrastructureManager.MatchMaking.MatchMaker;
import InfrastructureManager.ModuleManagement.ModuleFactory;
import InfrastructureManager.ModuleManagement.PlatformModule;
import InfrastructureManager.NodeLimit.NodeResourceLimiter;
import InfrastructureManager.REST.Input.POSTInput;
import InfrastructureManager.REST.Output.ParametrizedGETOutput;
import InfrastructureManager.REST.RestServerRunner;
import InfrastructureManager.REST.Output.GETOutput;
import InfrastructureManager.SSH.SSHClient;
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

    private MasterConfigurationData data; //Configuration File Interface
    private Map<String, MasterInputInterface> inputInstances;
    private List<String> scenarios;
    private Map<String,MasterOutput> outputInstances;
    private boolean activateRestRunner = false;
    //TODO: consider making it a singleton

    public MasterConfigurator(String configurationFilePath) {
        ObjectMapper mapper = new ObjectMapper(); //Using Jackson Functionality
        try {
            //Map the contents of the JSON file to a java object
            this.data = mapper.readValue(new File(configurationFilePath), MasterConfigurationData.class);
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
    /*private void fillInputInstances() {
        MasterInputInterface input;
        for (IOConfigData inputData : this.data.getIoData().getInputs()) {
            if (inputData.getType().equals("Scenario")) {
                this.scenarios.add(inputData.getName());
            } else {
                if (this.outputInstances.containsKey(inputData.getName())) {
                    input = (MasterInputInterface) this.outputInstances.get(inputData.getName());
                } else {
                    input = this.getInputFromData(inputData);
                }
                this.inputInstances.put(inputData.getName(), input);
            }
        }
    }*/

    /**
     * Extracts output data from the configuration file, including ports
     */
    /*private void fillOutputInstances() {
        MasterOutput output;
        for (IOConfigData outputData : this.data.getIoData().getOutputs()) {
            if (this.inputInstances.containsKey(outputData.getName())) { //If an instance is input and output
                output = (MasterOutput) this.inputInstances.get(outputData.getName());
            } else {
                output = getOutputFromData(outputData);
            }
            this.outputInstances.put(outputData.getName(), output);
        }
    }*/

    /*private MasterOutput getOutputFromData(IOConfigData outputData) throws IllegalArgumentException {
        int port = outputData.getPort();
        String[] type = outputData.getType().split("-");
        switch (type[0]) {
            case "ConsoleOutput":
                return new ConsoleOutput(outputData.getName());
            case "MasterUtility" :
                return new MasterUtility(outputData.getName());
            case "ScenarioDispatcher" :
                return new ScenarioDispatcher(outputData.getName());
            case  "ScenarioEditor" :
                return new ScenarioEditor(outputData.getName());
            case "SSHClient" :
                return new SSHClient(outputData.getName());
            case "FileOutput" :
                return new FileOutput(outputData.getName());
            case "GenericGET":
                RestServerRunner.configure("RestServer",port != 0 ? port : 4567);
                activateRestRunner = true;
                String param = getRESTPathParameter(outputData.getAddress());
                if (param == null) {
                    return new GETOutput(outputData.getName(),outputData.getAddress());
                } else {
                    return new ParametrizedGETOutput(outputData.getName(), outputData.getAddress(), param);
                }
            case "MatchMaker" :
                return new MatchMaker(outputData.getName(),type.length > 1 ? type[1] : "random");
            case "NodeResourceLimiter" :
                return new NodeResourceLimiter(outputData.getName());
            case "AdvantEdgeClient" :
                return new AdvantEdgeClient(outputData.getName(),outputData.getAddress() ,port != 0 ? port : 80);
            default:
                throw new IllegalArgumentException("Invalid output in Configuration");
        }
    }

    private MasterInputInterface getInputFromData(IOConfigData inputData) throws IllegalArgumentException {
        int port = inputData.getPort();
        String[] type = inputData.getType().split("-");
        switch (type[0]) {
            case "ConsoleInput":
                return new ConsoleInput();
            case "NodeResourceLimiter" :
                return new NodeResourceLimiter(inputData.getName());
            case "MatchMaker":
                return new MatchMaker(inputData.getName(),type.length > 1 ? type[1] : "random");
            case "GenericPOST":
                CustomCommandIO data = (CustomCommandIO)inputData;
                RestServerRunner.configure("RestServer",port != 0 ? port : 4567);
                activateRestRunner = true;
                //TODO:CHANGE
                return (MasterInputInterface) new POSTInput(data.getAddress(),data.getCommand(),data.getInformation());
            default:
                throw new IllegalArgumentException("Invalid input in Configuration");
        }
    }

    private MasterInputInterface getInput(String inputName) {
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
    }*/

    //private String getRESTPathParameter(String address) {
    //    if (address.matches(".*/:\\w*$")) {
    //        return address.substring(address.indexOf("/:") + 2);
    //    } else {
    //        return null;
    //    }
    //}


    /**
     * Based on the configuration file, returns the runners that the master should use
     * @return ArrayList of Runner objects for the master to run
     */
    public List<Runner> getRunners(){
        /*List<Runner> result = new ArrayList<>();
        MasterInputInterface runnerInput;
        MasterOutput[] runnerOutputs;
        String runnerName = "Runner_";
        fillInputInstances();
        fillOutputInstances();

        if (activateRestRunner) {
            try {
                result.add(RestServerRunner.getInstance());
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }

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

        return result;*/
        return null;
    }

    public List<PlatformModule> getModules() {
        ModuleFactory factory = new ModuleFactory(this.data);
        List<PlatformModule> disconnectedModules = factory.getModules();
        return null;
    }

}
