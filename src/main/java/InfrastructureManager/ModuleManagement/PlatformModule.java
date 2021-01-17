package InfrastructureManager.ModuleManagement;

import InfrastructureManager.*;
import InfrastructureManager.ModuleManagement.Exception.IncorrectInputException;
import InfrastructureManager.ModuleManagement.Exception.ModulePausedException;
import InfrastructureManager.ModuleManagement.Exception.ModuleStoppedException;
import InfrastructureManager.ModuleManagement.RawData.ModuleConfigData;

import java.util.*;
import java.util.function.BiConsumer;

public abstract class PlatformModule {

    public enum ModuleState { INITIAL, PAUSED, RUNNING }

    protected MasterInput[] inputs;//PRIVATE WITH GETTER
    protected MasterOutput[] outputs;//PRIVATE WITH GETTER
    protected Map<String, List<Connection>> inputConnections;//PRIVATE WITH GETTER
    protected final List<Runner> inputRunners;//PRIVATE WITH GETTER
    protected final List<Thread> inputRunnerThreads; //PRIVATE WITH GETTER

    private String name;
    private volatile ModuleState state;

    protected PlatformModule() {
        this.state = ModuleState.INITIAL;
        this.inputRunners = new ArrayList<>();
        this.inputRunnerThreads = new ArrayList<>();
        this.inputConnections = new HashMap<>();
    }

    public abstract void configure(ModuleConfigData data);

    public void setName(String name) {
        this.name = name;
    }

    protected void setInputs(MasterInput... inputs) {
        this.inputs = inputs;
        for (MasterInput input : this.inputs) {
            Runner runner = new Runner(input.getName(),input);
            input.setRunner(runner);
        } //todo: add not replace
    }

    protected void setOutputs(MasterOutput... outputs) {
        this.outputs = outputs;
        reportStateToOutputs();
    } //todo: add not replace

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

    //This can be overridden for different modules
    protected BiConsumer<Runner,MasterInput> setRunnerOperation() {
        return (runner,input) -> {
            try {
                String fromInput = input.read();
                Master master = Master.getInstance();
                for (Connection c : runner.getConnections()) {
                    String mapping = master.execute(fromInput,c.getCommands()); //TODO: REMOVE, part of module
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
        Arrays.stream(outputs).forEach(o -> o.reportState(this.state));
    }
}
