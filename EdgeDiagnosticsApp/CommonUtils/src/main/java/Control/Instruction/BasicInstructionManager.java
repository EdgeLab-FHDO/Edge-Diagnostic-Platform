package Control.Instruction;

import Control.Exception.InstructionCreationException;

public interface BasicInstructionManager {
    Instruction createInstruction(String instructionJson) throws InstructionCreationException;

}
