package InfrastructureManager;

import java.util.Scanner;

public class ConsoleInput implements MasterInput{
    private final Scanner IN = new Scanner(System.in);
    @Override
    public String read() {
        return IN.nextLine();
    }
}
