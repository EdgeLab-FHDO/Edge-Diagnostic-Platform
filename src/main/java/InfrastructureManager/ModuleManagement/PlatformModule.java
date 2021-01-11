package InfrastructureManager.ModuleManagement;

import InfrastructureManager.*;
import InfrastructureManager.ModuleManagement.Exception.IncorrectInputException;

import java.util.*;
import java.util.function.BiConsumer;

public abstract class PlatformModule {

    public enum ModuleState { INITIAL, PAUSED, RUNNING }

    protected MasterInput[] inputs;
    protected MasterOutput[] outputs;
    protected Map<String, List<Connection>> inputConnections;
    protected final List<Runner> inputRunners;
    protected final List<Thread> inputRunnerThreads;

    private final String name;
    private volatile ModuleState state;

    protected PlatformModule(String name) {
        this.name = name;
        this.state = ModuleState.INITIAL;
        this.inputRunners = new ArrayList<>();
        this.inputRunnerThreads = new ArrayList<>();
        this.inputConnections = new HashMap<>();
    }

    protected void setInputs(MasterInput... inputs) {
        this.inputs = inputs;
        for (MasterInput input : this.inputs) {
            Runner runner = new Runner(input.getName(),input);
            input.setRunner(runner);
        }
    }

    protected void setOutputs(MasterOutput... outputs) {
        this.outputs = outputs;
        reportStateToOutputs();
    }

    public MasterInput[] getInputs() {
        return inputs;
    }

    public MasterOutput[] getOutputs() {
        return outputs;
    }

    public String getName() {
        return name;
    }

    public ModuleState getState() {
        return state;
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
        return Arrays.stream(inputs).map(MasterInput::getName).anyMatch(inputName::equals);
    }

    private void configureRunners() {
        for (MasterInput in : inputs) {
            if (inputConnections.containsKey(in.getName())) {
                Runner runner = in.getRunner();
                runner.setConnections(inputConnections.get(in.getName()));
                runner.setRunOperation(setRunnerOperation());
                inputRunners.add(runner);
            }
        }
    }

    //This can be overriden for different modules
    protected BiConsumer<Runner,MasterInput> setRunnerOperation() {
        return (runner,input) -> {
            try {
                String fromInput = input.read();
                Master master = Master.getInstance();
                for (Connection c : runner.getConnections()) {
                    String mapping = master.execute(fromInput,c.getCommands());
                    if (mapping != null) {
                        try {
                            c.getOut().write(mapping);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

    private void reportStateToOutputs() {
        Arrays.stream(outputs).forEach(o -> o.reportState(this.state));
    }
}
