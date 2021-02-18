package InfrastructureManager.ModuleManagement;

import InfrastructureManager.Configuration.CommandSet;
import InfrastructureManager.Configuration.Exception.ResponseNotDefinedException;
import InfrastructureManager.ModuleManagement.Exception.Creation.IncorrectInputException;
import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleExecutionException;
import InfrastructureManager.ModuleManagement.Exception.Execution.ModulePausedException;
import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleStoppedException;
import InfrastructureManager.ModuleManagement.RawData.ModuleConfigData;

import java.util.*;

/**
 * Abstract class to represent a platform module. Every concrete module implementation extends this class.
 *
 * Implements the {@link ImmutablePlatformModule} interface to provide a read-only facade.
 *
 * Modules are the main functional components of the system. A module is defined as a
 * collection of predefined inputs and outputs which also has data about their connection
 * either between each other or with other modules.
 *
 * The definition of different modules in the platforms configurator allows the system to be tailored in its functionalities to specific use cases.
 *
 * PlatformModules are containers of a sort for the different inputs and outputs (making them containers for the functionality)
 * A module:
 *  - Has a state that influences its operation (IO in paused modules for example can't be used)
 *  - Keep track of inputs and the outputs to which they are connected (in this or other module)
 *  - Creates, configures and deploys {@link Runner}objects to read from the inputs and trigger the outputs
 *  - Acts as "middleman" between its objects, allowing for access to shared resources in a safe manner.
 */
public abstract class PlatformModule implements ImmutablePlatformModule {

    /**
     * Enum to represent the state of a module. Can be
     * - INITIAL: If the module is just created
     * - RUNNING: If the module is executing
     * - PAUSED: If the module is paused
     */
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

    /**
     * Default {@link RunnerOperation} which is passed to all created runners. In here, the input (the one related to each runner)
     * is read for a command. This command is the processed according to the different connections of the input, and the
     * processed response is sent to the appropriate output to trigger events.
     *
     * Exceptions happening in the process are redirected to be handled in the {@link PlatformInput#response(ModuleExecutionException)} method.
     *
     * Finally, throws {@link ModuleStoppedException} in a way that when the exception is raised the runner finished execution and the module can stop.
     *
     * For modules in which runners should perform different operations, this can be overridden.
     */
    protected RunnerOperation runnerOperation = (runner,input) -> {
        try {
            String fromInput = input.read();
            if (fromInput == null) { //If an inputs sends a null, exit the runner (Scenario does this)
                runner.exit();
            } else {
                for (Connection c : runner.getConnections()) { //gets the connections for the runner's input
                    try {
                        String mapping = this.processCommand(fromInput,c.getCommands());
                        if (c.getOut().getOwnerModuleState() == ModuleState.PAUSED) {
                            throw new ModulePausedException("Cannot write to output, Module is paused");
                        }
                        c.getOut().execute(mapping); //Trigger the output with the processed command
                        input.response(null); //If no errors, pass null
                    } catch (ModuleExecutionException e) {
                        input.response(e); //If error, pass it
                    } catch (ResponseNotDefinedException ignored) {}
                }
            }

        } catch (InterruptedException e){ //If interrupted, exit.
            throw new ModuleStoppedException();
        }
    }; //For other functionalities can be overridden

    /**
     * Constructor of the class. Initialized the module with an INITIAL state.
     */
    protected PlatformModule() {
        this.state = ModuleState.INITIAL;
        this.inputs = new ArrayList<>();
        this.outputs = new ArrayList<>();
        this.inputConnections = new HashMap<>();
        this.inputRunners = new ArrayList<>();
        this.inputRunnerThreads = new ArrayList<>();
        this.inputRunnerMap = new HashMap<>();
    }

    /**
     * Configuration method for all modules. Perform module specific configuration based on raw data.
     * @param data Raw module data.
     */
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

    /**
     * Sets the name of the module
     * @param name Name to give this module.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Adds inputs to the module. It also configures the built-in {@link ModuleDebugInput} with a default name of "MODULE_NAME.debug"
     * @param inputs PlatformInput objects, separated by commas
     */
    protected void setInputs(PlatformInput... inputs) {
        List<PlatformInput> temporalList = new ArrayList<>(Arrays.asList(inputs));
        this.debugInput = new ModuleDebugInput(this,name + ".debug"); //Built in logger
        temporalList.add(this.debugInput);
        temporalList.forEach(i -> {
            //For each input, create a runner and keep track in the inputRunnerMap
            Runner r = new Runner(this,i.getName(), i);
            inputRunnerMap.put(i.getName(),r);
        });
        this.inputs.addAll(temporalList); //Append them to the input list
    }

    /**
     * Adds outputs to the module
     * @param outputs PlatformOutput objects, separated by commas.
     */
    protected void setOutputs(PlatformOutput... outputs) {
        this.outputs.addAll(Arrays.asList(outputs));
    }

    /**
     * Add a connection to this module. This method, normally called by a {@link ModuleConnector} allows to add
     * a connection to one of the inputs of this module based on its name
     * @param inputName Name of the input to connect
     * @param connection Connection to add to the input (Includes output and commands)
     * @throws IncorrectInputException If the input could not be found inside the module's inputs list
     */
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

    /**
     * Allows to restart a dead thread. Useful for restarting Runners that have finished their operations
     * @param index Index of the thread in the thread's list (A new thread will replace it)
     * @param runnerIndex Index of the runner in the runnerList. The new thread will run this runner
     */
    protected void restartThread(int index, int runnerIndex) {
        Runner runner = inputRunners.get(runnerIndex);
        inputRunnerThreads.set(index,new Thread(runner, runner.getName()));
        inputRunnerThreads.get(index).start();
    }

    /**
     * Allows to know if a thread is dead (Runner finished its execution)
     * @param index Index of the thread in the inputRunnerThreads list
     * @return true if the thread is dead, false otherwise
     */
    protected boolean isDeadThread(int index) {
        return !inputRunnerThreads.isEmpty() && !inputRunnerThreads.get(index).isAlive();
    }

    /**
     * Starts the module by creating and starting threads for the runners in each of the inputs
     */
    public void start() {
        if (state != ModuleState.RUNNING) {
            configureRunners();
            fillThreads();
            inputRunnerThreads.forEach(Thread::start);
        }
        state = ModuleState.RUNNING;
    }

    /**
     * Pauses the module. A paused module's inputs and outputs are also paused
     */
    public void pause() {
        inputRunners.forEach(Runner::pause);
        state = ModuleState.PAUSED;
        System.out.println("PAUSED: " + name);
    }

    /**
     * Resumes a paused module
     */
    public void resume() {
        inputRunners.forEach(Runner::resume);
        state = ModuleState.RUNNING;
        System.out.println("RESUMED: " + name);
    }

    /**
     * Stops a module and finishes all runners execution
     */
    public void stop() {
        inputRunnerThreads.forEach(Thread::interrupt);
        inputRunners.forEach(Runner::exit);
        System.out.println("STOPPED: " + name);
    }

    /**
     * Creates a thread for each of the runners (each related to one input)
     */
    private void fillThreads() {
        inputRunners.forEach(r -> inputRunnerThreads.add(new Thread(r,r.getName())));
    }

    /**
     * Check if the module has an input configured with the defined name
     * @param inputName Name of the input to search
     * @return true if the inputs is configured in the module, false otherwise
     */
    protected boolean hasInput(String inputName) {
        return this.getInputs().stream().map(PlatformInput::getName).anyMatch(inputName::equals);
    }

    /**
     * For each of the inputs of the module, the connected ones are searched and for each, its runner
     * is configured (added connections and runner operation) and added to the runner list to be run later.
     */
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

    /**
     * Based on the commands defined as part of the connection between an input and an output, processes
     * the input command and returns the command to send to the output.
     * @param fromInput Reading from an input
     * @param commands Commands defined in the connection between input and output
     * @return Response mapped for the passed input command
     * @throws ResponseNotDefinedException If no response can be found in the given command set for the given input command
     */
    public String processCommand(String fromInput, CommandSet commands) throws ResponseNotDefinedException {
        return commands.getResponse(fromInput);
    }

    /**
     * Given an input, fetches the runner related to it.
     * @param inputName Name of the input
     * @return Runner that is related to the passed input
     * @throws IncorrectInputException If the input could not be found inside the module's inputs list
     */
    protected Runner getRunnerFromInput(String inputName) throws IncorrectInputException {
        Runner result = this.inputRunnerMap.get(inputName);
        if (result == null) {
            throw new IncorrectInputException("Input not defined in module!");
        }
        return result;
    }
}
