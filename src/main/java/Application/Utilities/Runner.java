public class Runner implements Runnable {
    protected String name;

    protected final Object pauseLock = new Object(); //In order to pause the runnable
    protected volatile boolean paused = false; //Flag to check status and be able to pause
    protected volatile boolean exit = false; //Flag to check status and be able to exit
    protected volatile boolean running = false; //Flag to see runner status

    @Override
    public void run() {
        running = true;
        exit = false;
        while (!exit) {
            checkPause();
            runOperation();
        }
        running = false;
    }

    private void checkPause() {
        if (paused) {
            running = false;
            synchronized (pauseLock) { //Access the shared flag resource
                try {
                    pauseLock.wait(); //Runner Pauses until is notified by other thread
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
            }
        }
    }

    protected void runOperation() {
    }

    public void exit(){
        exit = true;
    }

    public void pause() {
        paused = true;
    }

    public void resume() {
        synchronized (pauseLock) {
            paused = false;
            running = true;
            pauseLock.notifyAll(); //Notifies using the shared resource
        }
    }

    public String getName() {
        return name;
    }

    public boolean isRunning() {
        return running;
    }

    public boolean isPaused() {
        return paused;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Runner runner = (Runner) o;
        return name.equals(runner.name);
    }
}