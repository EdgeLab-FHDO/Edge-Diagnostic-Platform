package InfrastructureManager.ModuleManagement;

import InfrastructureManager.Configuration.CommandSet;
import InfrastructureManager.Connection;
import InfrastructureManager.MasterInput;
import InfrastructureManager.MasterOutput;
import InfrastructureManager.ModuleManagement.Exception.IncorrectInputException;
import InfrastructureManager.Runner;

import java.util.*;
import java.util.stream.Collectors;

public class Module {

    public enum ModuleState { INITIAL, PAUSED, RUNNING };

    protected MasterInput[] inputs;
    protected MasterOutput[] outputs;
    protected Map<String, List<Connection>> inputConnections;

    private final String name;
    private final List<Thread> inputRunnerThreads;
    private final List<Runner> inputRunners;
    private volatile ModuleState state;

    protected Module(String name, MasterInput[] inputs, MasterOutput[] outputs) {
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
        fillRunners();
        connectRunners();
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

    private void fillRunners() {
        Arrays.stream(inputs).filter(i -> inputConnections.containsKey(i.getName()))
                .map(MasterInput::getRunner).forEach(inputRunners::add);
    }

    private void connectRunners() {
        for (String input : inputConnections.keySet()) {
            for (Runner r : inputRunners) {
                if (r.getInputName().equals(input)) {
                    //TODO: SEND CONNECTIONS TO RUNNER
                }
            }
        }
    }


}
