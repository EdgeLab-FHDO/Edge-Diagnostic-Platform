package Application.MarkerDetection.OpenCVClient;

import java.io.IOException;

public class LatencyReporterRunner implements Runnable {
    //TODO implement and use pause, exit and running values
    private final OpenCVClientOperator activeOperator;
    private final LatencyReporterOperator reporter;

    private volatile boolean exit = false;
    private volatile boolean running = false;

    public LatencyReporterRunner(String url) {
        activeOperator = OpenCVClientOperator.getInstance();
        reporter = new LatencyReporterOperator(url);
    }
    @Override
    public void run() {
        while(true) {
            String reportedBody = null;
            try {
                //TODO get reported body
                reportedBody = activeOperator.getReportBody();
                reporter.report(reportedBody);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                System.out.println(e);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println(e);
            }

        }
    }
}
