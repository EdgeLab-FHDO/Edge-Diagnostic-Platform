package Application.OpenCVServer;

import Application.Utilities.DetectMarker;

public class OpenCVServer {
    private Thread serverThread;
    private Thread heartBeatThread;

    public static void main(String[] args) {
        DetectMarker.initOpenCVSharedLibrary();
        OpenCVServer activeServer = new OpenCVServer();
        OpenCVServerOperator activeOperator = OpenCVServerOperator.getInstance();

        activeOperator.setupServerRunners(args);
        activeServer.serverThread = new Thread(activeOperator.serverRunner);
        activeServer.serverThread.start();
        activeServer.heartBeatThread = new Thread(activeOperator.beatRunner);
        activeServer.heartBeatThread.start();

        try {
            activeServer.serverThread.join();
            activeServer.heartBeatThread.join();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}