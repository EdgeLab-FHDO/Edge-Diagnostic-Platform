package InfrastructureManager.Modules.Diagnostics;

import InfrastructureManager.ModuleManagement.PlatformModule;
import InfrastructureManager.ModuleManagement.RawData.ModuleConfigData;
import InfrastructureManager.Modules.Diagnostics.Input.InstructionInput;
import InfrastructureManager.Modules.Diagnostics.Output.InstructionOutput;

public class DiagnosticsModule extends PlatformModule implements GlobalVarAccessDiagnosticsModule {

    private final InstructionList instructionList;

    public DiagnosticsModule() {
        super();
        this.instructionList = new InstructionList(this);
    }

    @Override
    public void configure(ModuleConfigData data) {
        String name = data.getName();
        setName(name);
        setInputs(new InstructionInput(this,name + ".instructions.in"));
        setOutputs(new InstructionOutput(this,name + ".instructions.out"));
    }

    @Override
    public InstructionList getInstructionList() {
        return this.instructionList;
    }
}
