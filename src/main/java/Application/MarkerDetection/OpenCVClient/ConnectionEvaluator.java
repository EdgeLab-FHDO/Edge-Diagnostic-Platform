package Application.MarkerDetection.OpenCVClient;

import jdk.jshell.spi.ExecutionControlProvider;

import javax.sound.midi.SysexMessage;

public class ConnectionEvaluator {
    protected boolean evaluation;
    protected boolean evaluating;
    protected boolean disconnect;

    public ConnectionEvaluator() {
        evaluation = false;
        evaluating = false;
        disconnect = false;
    }

    public void evaluate() {
        evaluation = true; //this will eventually be refactored into a mock Connection Evaluator for sampling purpose
    }

    public boolean isGood() {
        return evaluation;
    }

    public boolean isEvaluating() { return evaluating; }

    public boolean needToDisconnect() { return disconnect; }

    public void setDisconnect(boolean disconnect) {
        this.disconnect = disconnect;
    }

    public void initialize() {
        evaluation = true; //assume connection is okay before evaluating on first iteration
        evaluating = true;
        System.out.println("Evaluator initialized!");
    }

    public void handleException(Exception e) {
        e.printStackTrace();
    }

    public int getEvaluationParameter() {
        return 0;
    }
}