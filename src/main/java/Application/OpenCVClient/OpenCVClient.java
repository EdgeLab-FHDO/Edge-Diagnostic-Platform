package Application.OpenCVClient;

import Application.Utilities.DetectMarker;

public class OpenCVClient {
    private Thread processingThread;
    private Thread masterCommunicationThread;
    private Thread heartBeatThread;

    public static void main(String[] args) {
        DetectMarker.initOpenCVSharedLibrary();
        OpenCVClient activeClient = new OpenCVClient();
        OpenCVClientOperator activeOperator = OpenCVClientOperator.getInstance();

        try {
            activeOperator.setupClientRunners(args);
            activeClient.masterCommunicationThread = new Thread(activeOperator.masterCommunicationRunner, "MasterCommunicationThread");
            activeClient.masterCommunicationThread.start();
            activeClient.processingThread = new Thread(activeOperator.processingRunner, "ProcessingThread");
            activeClient.processingThread.start();
            activeClient.heartBeatThread = new Thread(activeOperator.beatRunner, "HeartBeatThread");
            activeClient.heartBeatThread.start();

            activeClient.masterCommunicationThread.join();
            activeClient.processingThread.join();
            activeClient.heartBeatThread.join();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}