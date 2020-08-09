package InfrastructureManager;

public class ConsoleOutput implements MasterOutput {
    @Override
    public void out(String response) {
        System.out.println(response);
    }
}
