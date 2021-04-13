package Application.MarkerDetection.OpenCVClient;

import jdk.jshell.spi.ExecutionControlProvider;

import javax.sound.midi.SysexMessage;

public class ConnectionEvaluator {
    protected boolean evaluation;
    protected boolean evaluating;

    public ConnectionEvaluator() {
        evaluation = false;
        evaluating = false;
    }

    public void evaluate() {
        evaluation = true; //this will eventually be refactored into a mock Connection Evaluator for sampling purpose
    }

    public boolean isGood() {
        return evaluation;
    }

    public boolean isEvaluating() { return evaluating; }

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