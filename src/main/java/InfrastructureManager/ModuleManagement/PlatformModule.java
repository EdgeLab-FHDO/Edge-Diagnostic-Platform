package InfrastructureManager.ModuleManagement;

import InfrastructureManager.*;
import InfrastructureManager.Configuration.CommandSet;
import InfrastructureManager.ModuleManagement.Exception.IncorrectInputException;
import InfrastructureManager.ModuleManagement.Exception.ModulePausedException;
import InfrastructureManager.ModuleManagement.Exception.ModuleStoppedException;
import InfrastructureManager.ModuleManagement.RawData.ModuleConfigData;

import java.util.*;
import java.util.function.BiConsumer;

public abstract class PlatformModule {

    public enum ModuleState { INITIAL, PAUSED, RUNNING }

    private final List<ModuleInput> inputs;
    private final List<ModuleOutput> outputs;
    private String name;
    private final Map<String, List<Connection>> inputConnections;
    private final List<Runner> inputRunners;
    private final List<Thread> inputRunnerThreads;
    private volatile ModuleState state;

    protected PlatformModule() {
        this.state = ModuleState.INITIAL;
        this.inputs = new ArrayList<>();
        this.outputs = new ArrayList<>();
        this.inputRunners = new ArrayList<>();
        this.inputRunnerThreads = new ArrayList<>();
        this.inputConnections = new HashMap<>();
    }

    public abstract void configure(ModuleConfigData data);

    public void setName(String name) {
        this.name = name;
    }

    protected void setInputs(ModuleInput... inputs) {
        for (ModuleInput input : inputs) { //Process the new inputs by adding a runner
            Runner runner = new Runner(input.getName(),input);
            input.setRunner(runner);
        }
        this.inputs.addAll(Arrays.asList(inputs)); //Append them to the list
    }

    protected void setOutputs(ModuleOutput... outputs) {
        this.outputs.addAll(Arrays.asList(outputs));
        reportStateToOutputs();
    }

    public List<ModuleInput> getInputs() {
        return inputs;
    }

    public List<ModuleOutput> getOutputs() {
        return outputs;
    }

    public String getName() {
        return name;
    }

    public ModuleState getState() {
        return state;
    }

    public boolean isDeadThread(int index) {
        return !inputRunnerThreads.isEmpty() && !inputRunnerThreads.get(index).isAlive();
    }

    public void restartThread(int index, int runnerIndex) {
        Runner runner = inputRunners.get(runnerIndex);
        inputRunnerThreads.set(index,new Thread(runner, runner.getName()));
        inputRunnerThreads.get(index).start();
    }

    public void addConnection(String inputName, Connection connection) {
        if (hasInput(inputName)) {
            if (inputConnections.containsKey(inputName)) {
                inputConnections.get(inputName).add(connection);
            } else {
                List<Connection> list = new ArrayList<>();
                list.add(connection);
                inputConnections.put(inputName,list);
            }
        } else {
            throw new IncorrectInputException("Input not defined in module!");
        }

    }

    public void start() {
        configureRunners();
        fillThreads();
        inputRunnerThreads.forEach(Thread::start);
        state = ModuleState.RUNNING;
        reportStateToOutputs();
    }

    public void pause() {
        inputRunners.forEach(Runner::pause);
        state = ModuleState.PAUSED;
        reportStateToOutputs();
        System.out.println("PAUSED: " + name);
    }

    public void resume() {
        inputRunners.forEach(Runner::resume);
        state = ModuleState.RUNNING;
        reportStateToOutputs();
        System.out.println("RESUMED: " + name);
    }

    public void stop() {
        inputRunnerThreads.forEach(Thread::interrupt);
        inputRunners.forEach(Runner::exit);
        System.out.println("STOPPED: " + name);
    }

    private void fillThreads() {
        inputRunners.forEach(r -> inputRunnerThreads.add(new Thread(r,r.getName())));
    }

    private boolean hasInput(String inputName) {
        return inputs.stream().map(ModuleInput::getName).anyMatch(inputName::equals);
    }

    private void configureRunners() {
        for (ModuleInput in : inputs) {
            if (inputConnections.containsKey(in.getName())) {
                Runner runner = in.getRunner();
                runner.setConnections(inputConnections.get(in.getName()));
                runner.setRunOperation(setRunnerOperation());
                inputRunners.add(runner);
            }
        }
    }

    public String execute(String fromInput, CommandSet commands) {
        return commands.getResponse(fromInput);
    }

    //This can be overridden for different modules
    protected BiConsumer<Runner, ModuleInput> setRunnerOperation() {
        return (runner,input) -> {
            try {
                String fromInput = input.read();
                for (Connection c : runner.getConnections()) {
                    String mapping = this.execute(fromInput,c.getCommands());
                    if (mapping != null) {
                        try {
                            c.getOut().write(mapping);
                        } catch (IllegalArgumentException | ModulePausedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (InterruptedException e){
                throw new ModuleStoppedException();
            }
        };
    }

    private void reportStateToOutputs() {
        outputs.forEach(o -> o.reportState(this.state));
    }
}
