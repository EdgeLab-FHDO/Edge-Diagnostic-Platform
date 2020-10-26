package InfrastructureManager.Configuration.RawData;

import java.util.List;

public class IORawData {

    private List<IOConfigData> inputs;
    private List<IOConfigData> outputs;

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
