package InfrastructureManager.Configuration.RawData;

import java.util.List;

public class IORawData {

    private final List<IOConfigData> inputs;
    private final List<IOConfigData> outputs;

    public IORawData() {
        this.inputs = null;
        this.outputs = null;
    }

    public List<IOConfigData> getInputs() {
        return inputs;
    }

    public List<IOConfigData> getOutputs() {
        return outputs;
    }

}
