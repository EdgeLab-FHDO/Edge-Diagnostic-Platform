package InfrastructureManager;

public class ScenarioRunner implements Runnable {
    private Scenario scenario;
    private final Object pauseLock = new Object();
    private volatile boolean paused = false;
    public ScenarioRunner(Scenario scenario) {
        this.scenario = scenario;
    }
    @Override
    public void run() {
        Master master = Master.getInstance();
        for (Event e : this.scenario.getEventList()) {
            String mapping = master.execute(e.read());
            System.out.println(mapping);
            try {
                Thread.sleep(2000); //Just to see it sequentially in the console
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
            if (paused) {
                synchronized (pauseLock) {
                    try {
                        pauseLock.wait();
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                }
            }
        }
    }
    public void pause() {
        paused = true;
    }
    public void resume() {
        synchronized (pauseLock) {
            paused = false;
            pauseLock.notifyAll();
        }
    }

}
