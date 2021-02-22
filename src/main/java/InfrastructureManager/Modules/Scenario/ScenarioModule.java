package InfrastructureManager.Modules.Scenario;

import InfrastructureManager.ModuleManagement.Exception.Creation.IncorrectInputException;
import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;
import InfrastructureManager.ModuleManagement.PlatformModule;
import InfrastructureManager.ModuleManagement.RawData.ModuleConfigData;
import InfrastructureManager.ModuleManagement.Runner;
import InfrastructureManager.Modules.Scenario.Exception.Input.InvalidTimeException;
import InfrastructureManager.Modules.Scenario.Exception.Input.OwnerModuleNotSetUpException;
import InfrastructureManager.Modules.Scenario.Exception.ScenarioNotSetUpException;
import InfrastructureManager.Modules.Scenario.RawData.ScenarioModuleConfigData;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;


/**
 * {@link PlatformModule} that adds Scenario manipulation functionality to the platform.
 * <p>
 * This module gives the platform the functionality of definition, editing and deployment of {@link Scenario}, defined as a series of events that happen with a defined and guaranteed timing.
 * <p>
 * Each Scenario Module corresponds to one Scenario, and provides the necessary inputs and outputs to load, edit, run and save it.
 * <p>
 * Provides one Scenario input hardcoded with the name ".scenario", one dispatcher output with the name ".dispatcher"
 * and one editor output named ".editor"
 *
 * @see <a href="https://github.com/EdgeLab-FHDO/Edge-Diagnostic-Platform/wiki/Scenario-Module">Wiki Entry</a>
 */
public class ScenarioModule extends PlatformModule implements GlobalVarAccessScenarioModule {

    private Scenario scenario;

    /**
     * Constructor of the class. Creates a new Scenario module.
     */
    public ScenarioModule() {
        super();
    }

    /**
     * Based on raw module data, configures the module to use it. In this case, extracts the name and the scenario path
     * and creates the input (Scenario) by parsing it from the scenario file. This scenario, apart from being defined as input,
     * will be a shared resource between the two outputs. It also creates the outputs with their predefined names.
     *
     * @param data Raw module data.
     */
    @Override
    public void configure(ModuleConfigData data) {
        ScenarioModuleConfigData castedData = (ScenarioModuleConfigData) data;
        this.setName(castedData.getName());
        try {
            Scenario scenario = scenarioFromFile(castedData.getPath());
            setInputs(scenario);
            this.scenario = (Scenario) this.getInputs().get(0);
            setOutputs(new ScenarioEditor(this,this.getName() + ".editor"),
                    new ScenarioDispatcher(this,this.getName() + ".dispatcher"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Starts the Scenario of this module.
     *
     * @param startTime The start time of the scenario. This is an absolute time input (Milliseconds since UNIX epoch)
     * @throws InvalidTimeException         If the starting time is in the past when compared with the time when this method was called
     * @throws OwnerModuleNotSetUpException If the Owner module of the scenario was not set up during instantiation
     */
    public void startScenario(long startTime) throws InvalidTimeException, OwnerModuleNotSetUpException {
        if (isDeadThread(0)) {
            restartThread(0,0); //If the scenario already has already run once, restart the thread
        }
        scenario.setStartTime(startTime);
        scenario.start();
    }

    /**
     * Stops the Scenario of this module (and its assigned Runner).
     *
     * @throws ScenarioNotSetUpException If no runner is set up for the Scenario
     */
    public void stopScenario() throws ScenarioNotSetUpException {
        scenario.stop();
        getScenarioRunner().exit();
    }

    /**
     * Pauses the Scenario of this module (and its assigned Runner).
     *
     * @throws ScenarioNotSetUpException If no runner is set up for the Scenario
     */
    public void pauseScenario() throws ScenarioNotSetUpException {
        scenario.pause();
        getScenarioRunner().pause();
    }

    /**
     * Resumes the Scenario of this module (and its assigned Runner).
     *
     * @throws ScenarioNotSetUpException If no runner is set up for the Scenario.
     */
    public void resumeScenario() throws ScenarioNotSetUpException {
        scenario.resume();
        getScenarioRunner().resume();
    }

    /**
     * Given a file path, uses an {@link ObjectMapper} to deserialize the content in the file to a Scenario object
     *
     * @param path Path of the JSON scenario file
     * @return Scenario object created from the file
     * @throws IOException If an error occurs while parsing the file.
     */
    private Scenario scenarioFromFile(String path) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        InjectableValues inject = new InjectableValues.Std()
                .addValue(ImmutablePlatformModule.class, this);
        mapper.setInjectableValues(inject); //Inject owner module in constructor (because is not in the file)
        return mapper.readValue(new File(path),Scenario.class);
    }

    /**
     * Returns the Scenario of this module.
     *
     * This method can only be accessed if the reference to this module is through the {@link GlobalVarAccessScenarioModule} interface.
     *
     * @return The scenario of this module
     */
    @Override
    public Scenario getScenario() {
        return scenario;
    }

    /**
     * Returns the runner assigned to the Scenario of this module.
     *
     * @return Runner corresponding to particular Scenario
     * @throws ScenarioNotSetUpException If no runner is assigned to the Scenario
     */
    private Runner getScenarioRunner() throws ScenarioNotSetUpException {
        try {
            return this.getRunnerFromInput(this.getName() + ".scenario");
        } catch (IncorrectInputException e) {
            throw new ScenarioNotSetUpException("Scenario has not been set up in module " + this.getName());
        }
    }
}
