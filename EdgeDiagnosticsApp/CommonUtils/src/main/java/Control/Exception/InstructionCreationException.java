package Control.Exception;

public class InstructionCreationException extends ControlThreadException {
   public InstructionCreationException(Throwable cause) {
        super("Instruction could not be created", cause);
    }
}
