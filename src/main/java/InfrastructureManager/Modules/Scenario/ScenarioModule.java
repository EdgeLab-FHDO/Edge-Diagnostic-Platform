package InfrastructureManager.Modules.Scenario;

import InfrastructureManager.ModuleManagement.PlatformModule;
import InfrastructureManager.ModuleManagement.RawData.ModuleConfigData;
import InfrastructureManager.Modules.Scenario.Exception.Input.InvalidTimeException;
import InfrastructureManager.Modules.Scenario.RawData.ScenarioModuleConfigData;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class ScenarioModule extends PlatformModule {

    private Scenario scenario;

    public ScenarioModule() {
        super();
    }

    @Override
    public void configure(ModuleConfigData data) {
        ScenarioModuleConfigData castedData = (ScenarioModuleConfigData) data;
        this.setName(castedData.getName());
        try {
            Scenario scenario = scenarioFromFile(castedData.getPath());
            setInputs(scenario);
            this.scenario = (Scenario) this.getInputs().get(0);
            setOutputs(new ScenarioEditor(this.getName() + ".editor"),
                    new ScenarioDispatcher(this.getName() + ".dispatcher",scenario));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startScenario(long startTime) throws InvalidTimeException {
        if (isDeadThread(0)) {
            restartThread(0,0);
        }
        scenario.setStartTime(startTime);
        scenario.start();
    }

    public void stopScenario() {
        scenario.stop();
    }

    public void pauseScenario() {
        scenario.pause();
    }

    public void resumeScenario() {
        scenario.resume();
    }

    private Scenario scenarioFromFile(String path) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(new File(path),Scenario.class);
    }
}
