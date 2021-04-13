package Application.MarkerDetection.OpenCVClient;

import Application.Utilities.DetectMarker;
import Application.Utilities.ImageProcessor;
import Application.Utilities.RemoteExecutionException;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.opencv.core.Mat;

public class ProcessingOperator {
    private final OpenCVClientOperator activeOperator;
    private DetectMarker detector;

    public ProcessingOperator() {
        activeOperator = OpenCVClientOperator.getInstance();
        detector = new DetectMarker();
    }

    public void mainOperation() throws JsonProcessingException, InterruptedException {
        long startTime = System.nanoTime();
        boolean serverUsage = false;

        try {
            activeOperator.serverLock.acquire();
            serverUsage = activeOperator.utilizeServer;
            activeOperator.markerDetection(false, serverUsage, detector);
        } catch (RemoteExecutionException e) {
            activeOperator.evaluator.handleException(e);
        } finally {
            activeOperator.serverLock.release();
        }
        long endTime = System.nanoTime();
        activeOperator.queueLatencyReport(serverUsage, startTime, endTime);
    }
}
