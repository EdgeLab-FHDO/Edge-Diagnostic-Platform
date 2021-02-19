package InfrastructureManager.Modules.Utility;

import InfrastructureManager.ModuleManagement.PlatformModule;
import InfrastructureManager.ModuleManagement.RawData.ModuleConfigData;
import InfrastructureManager.Modules.Utility.Output.FileOutput;
import InfrastructureManager.Modules.Utility.Output.ModuleController;

/**
 * {@link PlatformModule} that adds control over Modules and file output functionalities to the platform.
 *
 * This module provides outputs that provide access to internal utilities of the platform.
 * This includes control over the platform's execution, as well as control over other modules.
 * It also provides the output to generate a file.
 *
 * Provides one ModuleController output hardcoded with the name ".control"
 * And one fileOutput with the name ".fileOut"
 *
 * @see <a href="https://github.com/EdgeLab-FHDO/Edge-Diagnostic-Platform/wiki/Utility-Module">Wiki Entry</a>
 */
public class UtilityModule extends PlatformModule {
    /**
     * Creates a new Utility module.
     */
    public UtilityModule() {
        super();
    }

    /**
     * Based on raw module data, configures the module to use it. In this case, extracts the name and creates
     * outputs with their predefined names.
     * @param data Raw module data
     */
    @Override
    public void configure(ModuleConfigData data) {
        this.setName(data.getName());
        setInputs(); //NO INPUTS
        setOutputs(new ModuleController(this,this.getName() + ".control"),
                new FileOutput(this,this.getName() + ".fileOut"));
    }
}
