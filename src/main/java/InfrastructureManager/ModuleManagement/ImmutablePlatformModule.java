package InfrastructureManager.ModuleManagement;

import InfrastructureManager.ModuleManagement.RawData.ModuleConfigData;

import java.util.*;

public abstract class ImmutablePlatformModule {
    public enum ModuleState { INITIAL, PAUSED, RUNNING }

    protected final List<PlatformInput> inputs;
    protected final List<PlatformOutput> outputs;
    protected final Map<String, List<Connection>> inputConnections;
    protected String name;
    protected ModuleDebugInput debugInput;
    protected volatile ModuleState state;


    public ImmutablePlatformModule() {
        this.state = ModuleState.INITIAL;
        this.inputs = new ArrayList<>();
        this.outputs = new ArrayList<>();
        this.inputConnections = new HashMap<>();
    }

    public abstract void configure(ModuleConfigData data);

    public List<PlatformInput> getInputs() {
        return Collections.unmodifiableList(inputs);
    }

    public List<PlatformOutput> getOutputs() {
        return Collections.unmodifiableList(outputs);
    }

    public String getName() {
        return name;
    }

    public ModuleState getState() {
        return state;
    }

    protected Map<String, List<Connection>> getInputConnections() {
        return Collections.unmodifiableMap(inputConnections);
    }

    public ModuleDebugInput getDebugInput() {
        return debugInput;
    }


}
