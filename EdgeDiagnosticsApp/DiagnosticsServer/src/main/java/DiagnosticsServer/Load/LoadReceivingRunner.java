package DiagnosticsServer.Load;

import Control.Instruction.InitialInstruction;
import Control.Instruction.Instruction;
import DiagnosticsServer.Control.ServerInstruction;
import DiagnosticsServer.Load.Exception.LoadReceivingException;
import DiagnosticsServer.Server;
import RunnerManagement.AbstractRunner;
import Control.Instruction.InstructionQueue;

public class LoadReceivingRunner extends AbstractRunner {

    private final ServerLoadManager manager;
    private final InstructionQueue instructionQueue;

    public LoadReceivingRunner(int port, InstructionQueue instructionQueue) {
        this.manager = new ServerLoadManager(port);
        this.instructionQueue = instructionQueue;
    }

    @Override
    public void runnerOperation() throws InterruptedException {
        Instruction instruction = instructionQueue.get();
        if (instruction.getClass().equals(InitialInstruction.class)) {
            return;
        } else {
            ServerInstruction serverInstruction = (ServerInstruction) instruction;
            manager.setSocketOptions(serverInstruction.getSocketOptions());
            manager.setConnectionType(serverInstruction.getConnectionType());
            manager.setLoadType(serverInstruction.getLoadType());
            try {
                manager.receiveLoad(serverInstruction.getConnection().getBufferInformation());
            } catch (LoadReceivingException e) {
                e.printStackTrace();
                this.stop();
            }
        }
        Thread.sleep(100);
    }
}
