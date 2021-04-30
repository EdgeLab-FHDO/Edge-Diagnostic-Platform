package Application.MarkerDetection.OpenCVClient;

import Application.Utilities.DetectMarker;
import Application.Utilities.RemoteExecutionException;

public class ServerEvaluationOperator {
    private final OpenCVClientOperator activeOperator;
    private final EQREvaluator evaluator;
    private DetectMarker detector;

    public ServerEvaluationOperator() {
        activeOperator = OpenCVClientOperator.getInstance();
        evaluator = (EQREvaluator) activeOperator.masterCommunicationRunner.evaluator;
        detector = new DetectMarker();
    }

    public void evaluationOperation() throws RemoteExecutionException {
        long startTime = System.nanoTime();

        activeOperator.markerDetection(true, true, detector);

        long endTime = System.nanoTime();
        int latency = (int)(endTime-startTime)/1000000;

        evaluator.updateLatency(latency);
    }
}
