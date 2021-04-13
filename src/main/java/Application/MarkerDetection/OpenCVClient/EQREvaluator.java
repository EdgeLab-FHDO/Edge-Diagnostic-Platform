package Application.MarkerDetection.OpenCVClient;

import java.util.concurrent.Semaphore;

public class EQREvaluator extends ConnectionEvaluator {
    private int max_eqr;
    private int current_eqr;
    private final Semaphore eqrBlock;

    public EQREvaluator() {
        super();
        eqrBlock = new Semaphore(1);
    }

    public void evaluate() {
        if(current_eqr == max_eqr) {
            evaluating = false;
        } else if(current_eqr == 0) {
            evaluation = false;
            evaluating = false;
        } else {
            evaluating = true;
        }
        System.out.println("[Node Evaluation] evaluation: " + evaluation + " evaluating: " + evaluating);
    }

    public void decrementEQR() {
        try {
            eqrBlock.acquire();
            current_eqr--;
            if (current_eqr < 0) {
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
