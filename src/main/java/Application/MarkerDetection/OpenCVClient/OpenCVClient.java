package Application.MarkerDetection.OpenCVClient;

import Application.Utilities.OpenCVUtil;

public class OpenCVClient {
    //TODO implement debug mode
    private Thread processingThread;
    private Thread masterCommunicationThread;
    private Thread heartBeatThread;

    public static void main(String[] args) {
        OpenCVUtil.initOpenCVSharedLibrary();
        OpenCVClient activeClient = new OpenCVClient();
        OpenCVClientOperator activeOperator = OpenCVClientOperator.getInstance();

        try {
            activeOperator.setupClientRunners(args);
            activeClient.heartBeatThread = new Thread(activeOperator.beatRunner, "HeartBeatThread");
            activeClient.heartBeatThread.start();
            activeClient.masterCommunicationThread = new Thread(activeOperator.masterCommunicationRunner, "MasterCommunicationThread");
            activeClient.masterCommunicationThread.start();
            activeClient.processingThread = new Thread(activeOperator.processingRunner, "ProcessingThread");
            activeClient.processingThread.start();

            activeClient.heartBeatThread.join();
            activeClient.masterCommunicationThread.join();
            activeClient.processingThread.join();
        } catch (IllegalArgumentException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}