package InfrastructureManager.Modules.Scenario;

import InfrastructureManager.*;
import InfrastructureManager.ModuleManagement.PlatformModule;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.function.BiConsumer;

public class ScenarioModule extends PlatformModule {

    private final ObjectMapper mapper;
    private Scenario scenario;

    public ScenarioModule(String name, String path) {
        super(name);
        mapper = new ObjectMapper();
        try {
            Scenario scenario = scenarioFromFile(path);
            setInputs(scenario);
            this.scenario = (Scenario) inputs[0];
            setOutputs(new ScenarioEditor(name + ".editor"),
                    new ScenarioDispatcher(name + ".dispatcher",scenario));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startScenario(long startTime) {
        if (!inputRunnerThreads.isEmpty() && !inputRunnerThreads.get(0).isAlive()) {
            inputRunnerThreads.set(0,new Thread(inputRunners.get(0)));
            inputRunnerThreads.get(0).start();
        }
        scenario.setStartTime(startTime);
        scenario.start();
        scenario.next(); //Unblock the scenario
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
        return mapper.readValue(new File(path),Scenario.class);
    }

    @Override
    protected BiConsumer<Runner, MasterInput> setRunnerOperation() {
        return (runner, input) -> {
            Scenario scenario = (Scenario)input;
            super.setRunnerOperation().accept(runner, input);
            scenario.next();
        };
    }
}
