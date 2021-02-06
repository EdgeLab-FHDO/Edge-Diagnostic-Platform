package InfrastructureManager.ModuleManagement;

import InfrastructureManager.Configuration.CommandSet;
import InfrastructureManager.Configuration.Exception.ResponseNotDefinedException;
import InfrastructureManager.ModuleManagement.Exception.Creation.IncorrectInputException;
import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleExecutionException;
import InfrastructureManager.ModuleManagement.Exception.Execution.ModulePausedException;
import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleStoppedException;
import InfrastructureManager.ModuleManagement.RawData.ModuleConfigData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public abstract class PlatformModule extends ImmutablePlatformModule {

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
        super();
        this.inputRunners = new ArrayList<>();
        this.inputRunnerThreads = new ArrayList<>();
    }

    public abstract void configure(ModuleConfigData data);

    public void setName(String name) {
        this.name = name;
    }

    protected void setInputs(PlatformInput... inputs) {
        List<PlatformInput> temporalList = new ArrayList<>(Arrays.asList(inputs));
        this.debugInput = new ModuleDebugInput(this,name + ".debug");
        temporalList.add(this.debugInput);
        temporalList.forEach(i -> i.setRunner(new Runner(this,i.getName(), i)));
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
            if (this.inputConnections.containsKey(in.getName())) {
                Runner runner = in.getRunner();
                runner.setConnections(this.inputConnections.get(in.getName()));
                runner.setRunOperation(this.runnerOperation);
                inputRunners.add(runner);
            }
        }
    }

    public String processCommand(String fromInput, CommandSet commands) throws ResponseNotDefinedException {
        return commands.getResponse(fromInput);
    }
}
