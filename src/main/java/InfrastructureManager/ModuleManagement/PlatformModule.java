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

    private final String name;
    private final List<Thread> inputRunnerThreads;
    private final List<Runner> inputRunners;
    private volatile ModuleState state;

    protected PlatformModule(String name, MasterInput[] inputs, MasterOutput[] outputs) {
        this.name = name;
        this.inputs = inputs;
        this.outputs = outputs;
        this.state = ModuleState.INITIAL;
        this.inputRunners = new ArrayList<>();
        this.inputRunnerThreads = new ArrayList<>();
        this.inputConnections = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public ModuleState getState() {
        return state;
    }

    public void addConnection(String inputName, List<Connection> connections) {
        if (hasInput(inputName)) {
            inputConnections.put(inputName,connections);
        } else {
            throw new IncorrectInputException("Input not defined in module!");
        }

    }

    public void start() {
        configureRunners();
        fillThreads();
        inputRunnerThreads.forEach(Thread::start);
        state = ModuleState.RUNNING;
    }

    public void pause() {
        inputRunners.forEach(Runner::pause);
        state = ModuleState.PAUSED;
    }

    public void resume() {
        inputRunners.forEach(Runner::resume);
        state = ModuleState.RUNNING;
    }

    public void stop() {
        inputRunnerThreads.forEach(Thread::interrupt);
        inputRunners.forEach(Runner::exit);
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
    protected BiConsumer<Runner,String> setRunnerOperation() {
        return (runner,fromInput) -> {
            Master master = Master.getInstance();
            try {
                for (Connection c : runner.getConnections()) {
                    String mapping = master.execute(fromInput,c.getCommands());
                    if (mapping != null) {
                        try {
                            c.getOut().out(mapping);
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


}
