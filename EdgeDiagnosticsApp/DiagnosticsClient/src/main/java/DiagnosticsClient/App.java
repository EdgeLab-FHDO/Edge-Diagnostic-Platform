package DiagnosticsClient;

public class App {


    public static void main(String[] args) {
        System.out.print("Hello, you inputted: ");
        if (args.length > 0) {
            for (String a : args){
                System.out.print(a + " ");
            }
            System.out.println();
        } else {
            System.out.println("nothing");
        }
    }
}
