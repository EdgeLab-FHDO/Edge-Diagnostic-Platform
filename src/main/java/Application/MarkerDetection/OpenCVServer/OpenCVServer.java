package Application.MarkerDetection.OpenCVServer;

import Application.Utilities.OpenCVUtil;

public class OpenCVServer {
    //TODO implement debug mode
    private Thread serverThread;
    private Thread heartBeatThread;
    private Thread registrationThread;

    public static void main(String[] args) {
        OpenCVUtil.initOpenCVSharedLibrary();
        OpenCVServer activeServer = new OpenCVServer();
        OpenCVServerOperator activeOperator = OpenCVServerOperator.getInstance();

        activeOperator.setupServerRunners(args);
        activeServer.registrationThread = new Thread(activeOperator.registrationRunner, "RegistrationThread");
        activeServer.registrationThread.start();
        activeServer.serverThread = new Thread(activeOperator.serverRunner);
        activeServer.serverThread.start();
        activeServer.heartBeatThread = new Thread(activeOperator.beatRunner);
        activeServer.heartBeatThread.start();


        try {
            activeServer.registrationThread.join();
            activeServer.serverThread.join();
            activeServer.heartBeatThread.join();
        } catch (IllegalArgumentException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}