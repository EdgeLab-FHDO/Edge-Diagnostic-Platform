package Application.MarkerDetection.OpenCVClient;

import Application.Utilities.DetectMarker;
import Application.Utilities.RemoteExecutionException;

public class ServerEvaluationOperator {
    private final OpenCVClientOperator activeOperator;
    private final EQREvaluator evaluator;
    private int latencyThreshold;
    private DetectMarker detector;

    public ServerEvaluationOperator(int latencyThreshold) {
        activeOperator = OpenCVClientOperator.getInstance();
        evaluator = (EQREvaluator) activeOperator.masterCommunicationRunner.evaluator;
        detector = new DetectMarker();
        this.latencyThreshold = latencyThreshold;
    }

    public void evaluationOperation() throws RemoteExecutionException {
        long startTime = System.nanoTime();

        activeOperator.markerDetection(true, true, detector);

        long endTime = System.nanoTime();
        long latency = (endTime-startTime)/1000000;

        if(latency > latencyThreshold) {
            evaluator.decrementEQR();
        } else {
            evaluator.incrementEQR();
        }
        System.out.println("[Server Evaluation] Execution Time: " + latency + "ms - EQR: " + evaluator.getEvaluationParameter());
    }
}
