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

    public void printInfo() {
        System.out.println("Inputs: ");
        inputs.forEach(e -> System.out.println(e.getType()));
        System.out.println("Outputs: ");
        outputs.forEach(e -> System.out.println(e.getType()));
        System.out.println("Ports: ");
        ports.forEach(e -> System.out.println(e.getPort()));
    }
}
