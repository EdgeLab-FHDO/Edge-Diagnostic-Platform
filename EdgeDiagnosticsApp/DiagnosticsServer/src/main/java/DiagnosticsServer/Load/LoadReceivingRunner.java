package DiagnosticsServer.Load;

import Communication.Exception.RESTClientException;
import Control.Instruction.InitialInstruction;
import Control.Instruction.Instruction;
import Control.Instruction.InstructionQueue;
import DiagnosticsServer.Communication.ServerPlatformConnection;
import DiagnosticsServer.Control.ServerInstruction;
import DiagnosticsServer.Load.Exception.LoadReceivingException;
import RunnerManagement.AbstractRunner;

public class LoadReceivingRunner extends AbstractRunner {

    private final ServerLoadManager manager;
    private final InstructionQueue instructionQueue;

    public LoadReceivingRunner(ServerPlatformConnection connection, int port, InstructionQueue instructionQueue) {
        this.manager = new ServerLoadManager(connection, port);
        this.instructionQueue = instructionQueue;
    }

    @Override
    public void runnerOperation() throws InterruptedException {
        try {
            manager.signalNextInstruction();
            Instruction instruction = instructionQueue.get();
            if (instruction.getClass().equals(InitialInstruction.class)) {
                String name = ((InitialInstruction) instruction).getExperimentName();
                System.out.println("Starting " + name);
                return;
            } else {
                ServerInstruction serverInstruction = (ServerInstruction) instruction;
                manager.setSocketOptions(serverInstruction.getSocketOptions());
                manager.setConnectionType(serverInstruction.getConnectionType());
                manager.setLoadType(serverInstruction.getLoadType());
                manager.receiveLoad(serverInstruction.getConnection().getBufferInformation());
            }
            Thread.sleep(100);
        } catch (RESTClientException e) {
            System.err.println("Platform could not be reached for next instruction");
            this.stop();
        } catch (LoadReceivingException e) {
            e.printStackTrace();
            this.stop();
        }
    }
}
