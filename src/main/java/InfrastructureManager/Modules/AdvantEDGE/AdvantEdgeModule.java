package InfrastructureManager.Modules.AdvantEDGE;

import InfrastructureManager.ModuleManagement.RawData.ModuleConfigData;
import InfrastructureManager.Modules.AdvantEDGE.Output.AdvantEdgeClient;
import InfrastructureManager.ModuleManagement.PlatformModule;
import InfrastructureManager.Modules.AdvantEDGE.RawData.AdvantEdgeModuleConfigData;

public class AdvantEdgeModule  extends PlatformModule {

    public AdvantEdgeModule() {
        super();
    }

    @Override
    public void configure(ModuleConfigData data) {
        AdvantEdgeModuleConfigData castedData = (AdvantEdgeModuleConfigData) data;
        this.setName(castedData.getName());
        setInputs(); //No inputs
        setOutputs(new AdvantEdgeClient(this.getName() + ".out", castedData.getAddress(),
                castedData.getPort()));
    }
}
