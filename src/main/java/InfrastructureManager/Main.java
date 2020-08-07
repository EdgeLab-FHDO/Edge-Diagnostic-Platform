package InfrastructureManager;



public class Main {
    public static void main(String[] args) {
        Master master = new Master();
        System.out.println(master.execute("deploy application"));
    }
}
