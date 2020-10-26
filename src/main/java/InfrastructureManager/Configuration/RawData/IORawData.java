package InfrastructureManager.Configuration.RawData;

import java.util.List;

public class IORawData {

    private List<IOConfigData> inputs;
    private List<IOConfigData> outputs;
    private List<IOPortConfigData> ports;

    public IORawData() {
        this.inputs = null;
        this.outputs = null;
        this.ports = null;
    }

    public List<IOConfigData> getInputs() {
        return inputs;
    }

    public List<IOConfigData> getOutputs() {
        return outputs;
    }

    public List<IOPortConfigData> getPorts() {
        return ports;
    }
}
