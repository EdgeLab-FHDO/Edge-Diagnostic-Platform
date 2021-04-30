package Application.MarkerDetection.OpenCVClient;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class EQREvaluator extends ConnectionEvaluator {
    private int max_eqr;
    private int currentEqr;
    private final Semaphore eqrBlock;
    private final Semaphore eqrChangeBlock;
    private int currentLatency;

    public EQREvaluator() {
        super();
        eqrChangeBlock = new Semaphore(1);
        eqrBlock = new Semaphore(1);
        max_eqr = -1;
        currentEqr = 0;
        currentLatency = 0;
    }

    public void evaluate() {
        if(currentEqr == max_eqr) {
            evaluating = false;
        } else if(currentEqr == 0) {
            evaluation = false;
            evaluating = false;
            max_eqr=-1;
        } else {
            evaluating = true;
        }
    }

    public void updateLatency(int latency) {
        try {
            eqrChangeBlock.acquire();
            currentLatency=latency;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            eqrChangeBlock.release();
        }
    }

    public void manageEqrChange(int latencyThreshold) {
        try {
            eqrChangeBlock.acquire();
            // only generate new increment if at least one evaluation has been made
            if(currentLatency > 0) {
                if (currentLatency > latencyThreshold) {
                    decrementEQR();
                } else {
                    incrementEQR();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            eqrChangeBlock.release();
        }
    }

    public void decrementEQR() {
        try {
            eqrBlock.acquire();
            currentEqr--;
            if (currentEqr == 0) {
                setDisconnect(true);
            } else if (currentEqr < 0) {
                currentEqr = 0;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            eqrBlock.release();
        }
    }

    public void incrementEQR() {
        try {
            eqrBlock.acquire();
            currentEqr++;
            if(currentEqr > max_eqr) {
                currentEqr = max_eqr;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            eqrBlock.release();
        }
    }

    public void initialize() {
        evaluation = true; //assume connection is okay before evaluating on first iteration
        evaluating = true;
        OpenCVClientOperator activeOperator = OpenCVClientOperator.getInstance();
        try {
            eqrBlock.acquire();
            max_eqr = activeOperator.maxEdgeQualityRating;
            currentEqr = max_eqr/2;
            currentLatency = 0;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            eqrBlock.release();
        }
        System.out.println("EQR:"+ currentEqr +"/"+max_eqr);
    }

    public void handleException(Exception e) {
        decrementEQR();
    }

    public int getEvaluationParameter() {
        return currentEqr;
    }
}
