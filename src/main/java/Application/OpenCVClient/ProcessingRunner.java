public class ProcessingRunner extends Runner {
    // TO DO: move OpenCVClients functions to here or a new class
    @Override
    protected void runOperation() {
        OpenCVClient activeClient = OpenCVClient.getInstance();
        activeClient.markerDetection();
    }
}