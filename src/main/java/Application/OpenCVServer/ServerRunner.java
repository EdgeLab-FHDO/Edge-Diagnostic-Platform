public class ServerRunner implements Runnable {
    @Override
    public void run() {
        while(true) {
            OpenCVServer activeServer = OpenCVServer.getInstance();
            activeServer.startConnection();
            activeServer.standbyForConnection();
            activeServer.stopConnection();
        }
    }
}