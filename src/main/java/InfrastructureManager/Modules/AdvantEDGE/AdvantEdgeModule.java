package InfrastructureManager.Modules.AdvantEDGE;

import InfrastructureManager.ModuleManagement.RawData.ModuleConfigData;
import InfrastructureManager.Modules.AdvantEDGE.Output.AdvantEdgeClient;
import InfrastructureManager.ModuleManagement.PlatformModule;
import InfrastructureManager.Modules.AdvantEDGE.RawData.AdvantEdgeModuleConfigData;

/**
 * {@link PlatformModule} that adds interaction with AdvantEDGE as functionality to the platform.
 * <p>
 * This module allows integration with AdvantEDGE emulation platform providing control over network resources as well.
 * <p>
 * Provides one advantEdgeClient output hardcoded with the name ".out"
 *
 * @see <a href="https://github.com/EdgeLab-FHDO/Edge-Diagnostic-Platform/wiki/AdvantEDGE-Module">Wiki Entry</a>
 */
public class AdvantEdgeModule  extends PlatformModule {

    /**
     * Instantiates a new Advant edge module.
     */
    public AdvantEdgeModule() {
        super();
    }

    /**
     * Based on raw module data, configures the module to use it. In this case, extracts the name, address and port information
     * and creates the output with them, as well as assigns it its predefined name.
     * @param data Raw module data.
     */
    @Override
    public void configure(ModuleConfigData data) {
        AdvantEdgeModuleConfigData castedData = (AdvantEdgeModuleConfigData) data;
        this.setName(castedData.getName());
        setInputs(); //No inputs
        setOutputs(new AdvantEdgeClient(this, this.getName() + ".out", castedData.getAddress(),
                castedData.getPort()));
    }
}
