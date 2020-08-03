package hello;

public class SimpleApp {
    public String sayHello() {
        return "Hello World!";
    }

    public static void main(String[] args) {
        SimpleApp app = new SimpleApp();
        System.out.println(app.sayHello());
    }
}
