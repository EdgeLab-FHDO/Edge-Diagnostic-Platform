package InfrastructureManager.Modules.Diagnostics;

import InfrastructureManager.ModuleManagement.PlatformModule;
import InfrastructureManager.ModuleManagement.RawData.ModuleConfigData;
import InfrastructureManager.Modules.Diagnostics.Input.AdvantEdgeInstructionInput;
import InfrastructureManager.Modules.Diagnostics.Input.ApplicationInstructionInput;
import InfrastructureManager.Modules.Diagnostics.Input.ComputeInstructionInput;
import InfrastructureManager.Modules.Diagnostics.Output.InstructionOutput;

public class DiagnosticsModule extends PlatformModule implements GlobalVarAccessDiagnosticsModule {

    private final InstructionSet instructionList;

    public DiagnosticsModule() {
        super();
        this.instructionList = new InstructionSet(this);
    }

    @Override
    public void configure(ModuleConfigData data) {
        String name = data.getName();
        setName(name);
        setInputs(new ApplicationInstructionInput(this,name + ".instructions.in.app"),
                new AdvantEdgeInstructionInput(this,name +  ".instructions.in.ae"),
                new ComputeInstructionInput(this, name + ".instructions.in.compute"));
        setOutputs(new InstructionOutput(this,name + ".instructions.out"));
    }

    @Override
    public InstructionSet getInstructionList() {
        return this.instructionList;
    }
}
