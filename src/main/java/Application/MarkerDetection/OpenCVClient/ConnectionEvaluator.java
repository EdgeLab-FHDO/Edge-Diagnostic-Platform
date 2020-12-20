package Application.MarkerDetection.OpenCVClient;

import javax.sound.midi.SysexMessage;

public class ConnectionEvaluator {
    private boolean evaluation;

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