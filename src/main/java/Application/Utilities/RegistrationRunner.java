package Application.Utilities;

import Application.Commons.CoreOperator;

import java.io.IOException;

public class RegistrationRunner implements Runnable {
    //TODO implement and use exit and running values
    private final RegistrationOperator registrar;

    private volatile boolean exit = false;
    private volatile boolean running = true;

    public RegistrationRunner(CoreOperator activeOperator, String url, String body) {
        registrar = new RegistrationOperator(activeOperator, url, body);
    }
    @Override
    public void run() {
        while(!exit) {
            try {
                registrar.register();
                exit = true;
                running = false;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * When called, the runner terminates
     */
    public void exit(){
        exit = true;
    }

    /**
     * See if the current runner is running
     * @return True if the runner is running, otherwise false.
     */
    public boolean isRunning() {
        return running;
    }
}
