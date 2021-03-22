package DiagnosticsServer;

import DiagnosticsServer.Communication.ServerPlatformConnection;
import DiagnosticsServer.Control.ServerInstructionManager;
import Multithreading.BasicRunnerManager;
import Multithreading.ControlRunner;
import Multithreading.InstructionQueue;

public class ServerRunnerManager extends BasicRunnerManager {

    protected void configure(ServerPlatformConnection connection, String heartbeatBody, int port) {
        InstructionQueue instructionQueue = new InstructionQueue();
        ServerInstructionManager manager = new ServerInstructionManager();
        super.configure(connection, heartbeatBody);
        getRunners().add(new ControlRunner(connection,manager,instructionQueue));
        getRunners().add(new LoadReceivingRunner(port,instructionQueue));
    }
}
