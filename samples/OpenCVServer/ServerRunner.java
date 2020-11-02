public class ServerRunner extends Runner {
    @Override
    protected void runOperation() {
        OpenCVServer activeServer = OpenCVServer.getInstance();
        activeServer.startConnection();
        activeServer.standbyForConnection();
        activeServer.stopConnection();
    }
}