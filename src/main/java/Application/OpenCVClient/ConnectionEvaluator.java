package Application.OpenCVClient;

public class ConnectionEvaluator {
    protected boolean evaluation;

    public ConnectionEvaluator() {
        evaluation = true; //assume connection is okay before evaluating on first iteration
    }

    public void evaluate() {
        evaluation = true; //this will eventually be refactored into a mock Connection Evaluator for sampling purpose
    }

    public boolean isGood() {
        return evaluation;
    }
}