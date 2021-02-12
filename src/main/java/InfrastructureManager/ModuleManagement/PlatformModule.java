package InfrastructureManager.ModuleManagement;

import InfrastructureManager.Configuration.CommandSet;
import InfrastructureManager.Configuration.Exception.ResponseNotDefinedException;
import InfrastructureManager.ModuleManagement.Exception.Creation.IncorrectInputException;
import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleExecutionException;
import InfrastructureManager.ModuleManagement.Exception.Execution.ModulePausedException;
import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleStoppedException;
import InfrastructureManager.ModuleManagement.RawData.ModuleConfigData;

import java.util.*;

public abstract class PlatformModule implements ImmutablePlatformModule {

    public enum ModuleState { INITIAL, PAUSED, RUNNING }

    private final List<PlatformInput> inputs;
    private final List<PlatformOutput> outputs;
    private final Map<String, List<Connection>> inputConnections;
    private String name;
    private ModuleDebugInput debugInput;
    private volatile ModuleState state;

    private final Map<String,Runner> inputRunnerMap;

    private final List<Runner> inputRunners;
    private final List<Thread> inputRunnerThreads;

    protected RunnerOperation runnerOperation = (runner,input) -> {
        try {
            String fromInput = input.read();
            if (fromInput == null) {
                runner.exit();
            } else {
                for (Connection c : runner.getConnections()) {
                    try {
                        String mapping = this.processCommand(fromInput,c.getCommands());
                        if (c.getOut().getOwnerModuleState() == ModuleState.PAUSED) {
                            throw new ModulePausedException("Cannot write to output, Module is paused");
                        }
                        c.getOut().execute(mapping);
                        input.response(null);
                    } catch (ModuleExecutionException e) {
                        input.response(e);
                    } catch (ResponseNotDefinedException ignored) {}
                }
            }

        } catch (InterruptedException e){
            throw new ModuleStoppedException();
        }
    }; //For other functionalities can be overridden

    protected PlatformModule() {
        this.state = ModuleState.INITIAL;
        this.inputs = new ArrayList<>();
        this.outputs = new ArrayList<>();
        this.inputConnections = new HashMap<>();
        this.inputRunners = new ArrayList<>();
        this.inputRunnerThreads = new ArrayList<>();
        this.inputRunnerMap = new HashMap<>();
    }

    public abstract void configure(ModuleConfigData data);

    @Override
    public List<PlatformInput> getInputs() {
        return Collections.unmodifiableList(inputs);
    }

    @Override
    public List<PlatformOutput> getOutputs() {
        return Collections.unmodifiableList(outputs);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ModuleState getState() {
        return state;
    }

    @Override
    public Map<String, List<Connection>> getInputConnections() {
        return Collections.unmodifiableMap(inputConnections);
    }

    @Override
    public ModuleDebugInput getDebugInput() {
        return debugInput;
    }

    public void setName(String name) {
        this.name = name;
    }

    protected void setInputs(PlatformInput... inputs) {
        List<PlatformInput> temporalList = new ArrayList<>(Arrays.asList(inputs));
        this.debugInput = new ModuleDebugInput(this,name + ".debug");
        temporalList.add(this.debugInput);
        temporalList.forEach(i -> {
            Runner r = new Runner(this,i.getName(), i);
            inputRunnerMap.put(i.getName(),r);
        });
        this.inputs.addAll(temporalList); //Append them to the list
    }

    protected void setOutputs(PlatformOutput... outputs) {
        this.outputs.addAll(Arrays.asList(outputs));
    }

    public void addConnection(String inputName, Connection connection) throws IncorrectInputException {
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

    public void restartThread(int index, int runnerIndex) {
        Runner runner = inputRunners.get(runnerIndex);
        inputRunnerThreads.set(index,new Thread(runner, runner.getName()));
        inputRunnerThreads.get(index).start();
    }

    public boolean isDeadThread(int index) {
        return !inputRunnerThreads.isEmpty() && !inputRunnerThreads.get(index).isAlive();
    }

    public void start() {
        if (state != ModuleState.RUNNING) {
            configureRunners();
            fillThreads();
            inputRunnerThreads.forEach(Thread::start);
        }
        state = ModuleState.RUNNING;
    }

    public void pause() {
        inputRunners.forEach(Runner::pause);
        state = ModuleState.PAUSED;
        System.out.println("PAUSED: " + name);
    }

    public void resume() {
        inputRunners.forEach(Runner::resume);
        state = ModuleState.RUNNING;
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

    protected boolean hasInput(String inputName) {
        return this.getInputs().stream().map(PlatformInput::getName).anyMatch(inputName::equals);
    }

    private void configureRunners() {
        for (PlatformInput in : this.inputs) {
            String inputName = in.getName();
            if (this.inputConnections.containsKey(inputName)) {
                Runner runner = inputRunnerMap.get(inputName);
                runner.setConnections(this.inputConnections.get(inputName));
                runner.setRunOperation(this.runnerOperation);
                inputRunners.add(runner);
            }
        }
    }

    public String processCommand(String fromInput, CommandSet commands) throws ResponseNotDefinedException {
        return commands.getResponse(fromInput);
    }
}
