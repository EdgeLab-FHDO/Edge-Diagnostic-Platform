package InfrastructureManager.Modules.AdvantEDGE;

import InfrastructureManager.Modules.AdvantEDGE.Output.AdvantEdgeClient;
import InfrastructureManager.ModuleManagement.PlatformModule;

public class AdvantEdgeModule  extends PlatformModule {

    public AdvantEdgeModule(String name, int port, String address) {
        super(name);
        setInputs(); //No inputs
        setOutputs(new AdvantEdgeClient(name + ".out", address, port));
    }
}
