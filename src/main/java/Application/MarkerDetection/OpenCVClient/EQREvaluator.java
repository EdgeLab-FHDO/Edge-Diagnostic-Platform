package Application.MarkerDetection.OpenCVClient;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class EQREvaluator extends ConnectionEvaluator {
    private int max_eqr;
    private int current_eqr;
    private final Semaphore eqrBlock;
    private final Semaphore eqrChangeBlock;
    private Queue<Boolean> eqrChangeQueue;

    public EQREvaluator() {
        super();
        eqrChangeBlock = new Semaphore(1);
        eqrBlock = new Semaphore(1);
        eqrChangeQueue = new LinkedList<>();
        max_eqr=-1;
        current_eqr=0;
    }

    public void evaluate() {
        if(current_eqr == max_eqr) {
            evaluating = false;
        } else if(current_eqr == 0) {
            evaluation = false;
            evaluating = false;
            max_eqr=-1;
        } else {
            evaluating = true;
        }
        System.out.println("[Node Evaluation] evaluation: " + evaluation + " evaluating: " + evaluating);
    }

    public void queueEqrChange(boolean increment) {
        try {
            eqrChangeBlock.acquire();
            eqrChangeQueue.add(increment);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            eqrChangeBlock.release();
        }
    }

    public void manageEqrChange() {
        try {
            eqrChangeBlock.acquire();
            if(eqrChangeQueue.size() > 0) {
                boolean increment = eqrChangeQueue.remove();
                if (increment) {
                    incrementEQR();
                } else {
                    decrementEQR();
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
            current_eqr--;
            if (current_eqr == 0) {
                setDisconnect(true);
            } else if (current_eqr < 0) {
                current_eqr = 0;
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
            current_eqr++;
            if(current_eqr>max_eqr) {
                current_eqr = max_eqr;
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
            current_eqr = max_eqr/2;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            eqrBlock.release();
        }
        System.out.println("EQR:"+ current_eqr +"/"+max_eqr);
    }

    public void handleException(Exception e) {
        decrementEQR();
    }

    public int getEvaluationParameter() {
        return current_eqr;
    }
}
