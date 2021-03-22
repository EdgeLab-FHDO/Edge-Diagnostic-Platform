package DiagnosticsServer;

import DiagnosticsServer.Control.RawData.ServerInstruction;
import DiagnosticsServer.Load.Exception.LoadReceivingException;
import DiagnosticsServer.Load.ServerLoadManager;
import Multithreading.AbstractRunner;
import Multithreading.InstructionQueue;

public class LoadReceivingRunner extends AbstractRunner {

    private final ServerLoadManager manager;
    private final InstructionQueue instructionQueue;

    public LoadReceivingRunner(int port, InstructionQueue instructionQueue) {
        this.manager = new ServerLoadManager(port);
        this.instructionQueue = instructionQueue;
    }

    @Override
    public void runnerOperation() throws InterruptedException {
        ServerInstruction instruction = (ServerInstruction) instructionQueue.get();
        manager.setSocketOptions(instruction.getSocketOptions());
        manager.setConnectionType(instruction.getConnectionType());
        manager.setLoadType(instruction.getLoadType());
        Thread.sleep(100);
        try {
            manager.receiveLoad();
        } catch (LoadReceivingException e) {
            e.printStackTrace();
        }
    }
}
