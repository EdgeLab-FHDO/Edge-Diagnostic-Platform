package InfrastructureManager.Modules.Scenario;

import InfrastructureManager.*;
import InfrastructureManager.ModuleManagement.PlatformModule;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.function.BiConsumer;

public class ScenarioModule extends PlatformModule {

    private final ObjectMapper mapper;

    public ScenarioModule(String name, String path) {
        super(name);
        mapper = new ObjectMapper();
        try {
            Scenario scenario = scenarioFromFile(path);
            setInputs(scenario);
            setOutputs(new ScenarioEditor(name + ".editor", scenario),
                    new ScenarioDispatcher(name + ".dispatcher",scenario));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startScenario(long startTime) {
        Scenario scenario = (Scenario) inputs[0];
        scenario.setStartTime(startTime);
        scenario.start();
        scenario.next(); //Unblock the scenario
    }

    public void stopScenario() {
        this.inputRunners.forEach(Runner::exit);
    }

    public void pauseScenario() {
        this.inputRunners.forEach(Runner::pause);
    }

    public void resumeScenario() {
        this.inputRunners.forEach(Runner::resume);
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
            if (scenario.isFinished()) {
                runner.exit();
                System.out.println("FINISHED SCENARIO: " + scenario.getName());
            }
        };
    }
}
