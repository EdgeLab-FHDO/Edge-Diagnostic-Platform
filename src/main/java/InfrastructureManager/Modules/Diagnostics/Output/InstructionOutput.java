package InfrastructureManager.Modules.Diagnostics.Output;

import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleExecutionException;
import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;
import InfrastructureManager.ModuleManagement.PlatformOutput;
import InfrastructureManager.Modules.Diagnostics.DiagnosticsModuleObject;
import InfrastructureManager.Modules.Diagnostics.Exception.Instruction.InstructionFileLoadingException;
import InfrastructureManager.Modules.Diagnostics.Exception.Instruction.InstructionHandlingException;
import InfrastructureManager.Modules.Diagnostics.RawData.Instruction.Instruction;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class InstructionOutput extends DiagnosticsModuleObject implements PlatformOutput {

    public InstructionOutput(ImmutablePlatformModule ownerModule, String name) {
        super(ownerModule, name);
    }

    @Override
    public void execute(String response) throws ModuleExecutionException {
        String[] command = response.split(" ");
        if (command[0].equals("instruction")) {
            try {
                switch (command[1]) {
                    case "loadInstruction" -> {
                        Instruction instruction = instructionFromFile(command[2],command[3]);
                        addToInstructionList(instruction);
                    }
                    default -> throw new InstructionHandlingException("Invalid command " + command[1] + " for InstructionOutput");
                }

            } catch (IndexOutOfBoundsException e) {
                throw new InstructionHandlingException("Arguments missing for command " + response  + " to InstructionOutput");
            }
        }
    }

    private Instruction instructionFromFile(String clientInstructionPath, String serverInstructionPath) throws InstructionFileLoadingException {
        try {
            File clientFile = new File(clientInstructionPath);
            File serverFile = new File(serverInstructionPath);
            if (!clientFile.exists() || !serverFile.exists()) {
                throw new InstructionFileLoadingException("Instruction files were not found!");
            }
            String clientInstruction = Files.readString(clientFile.toPath()).replaceAll("\\s+","");
            String serverInstruction = Files.readString(serverFile.toPath()).replaceAll("\\s+","");
            return new Instruction(clientInstruction,serverInstruction);
        } catch (IOException e) {
            throw new InstructionFileLoadingException("Instruction files could not be read",e);
        }
    }

    private void addToInstructionList(Instruction instruction) throws InstructionHandlingException {
        try {
            this.getInstructionList().addInstruction(instruction);
        } catch (InterruptedException e) {
            throw new InstructionHandlingException("Output interrupted while waiting on instruction list");
        }
    }
}
