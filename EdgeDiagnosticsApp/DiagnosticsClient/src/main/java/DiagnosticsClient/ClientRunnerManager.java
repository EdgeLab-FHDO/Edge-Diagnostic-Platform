package DiagnosticsClient;

import DiagnosticsClient.Communication.ClientPlatformConnection;
import DiagnosticsClient.Communication.ServerInformation;
import DiagnosticsClient.Control.ClientInstructionManager;
import DiagnosticsClient.Load.Exception.TCP.ServerNotSetUpException;
import Multithreading.*;

public class ClientRunnerManager extends BasicRunnerManager {

    public ClientRunnerManager() {
        super();
    }

    public void configure(ClientPlatformConnection connection,
                          String heartBeatBody,
                          ServerInformation serverInformation) throws ServerNotSetUpException {
        InstructionQueue instructionQueue = new InstructionQueue();
        ClientInstructionManager manager = new ClientInstructionManager();
        super.configure(connection,heartBeatBody);
        getRunners().add(new ControlRunner(connection,manager,instructionQueue));
        getRunners().add(new LoadSendingRunner(connection,serverInformation, instructionQueue));
    }

}
