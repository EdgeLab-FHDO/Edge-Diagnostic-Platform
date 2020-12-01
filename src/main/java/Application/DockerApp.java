package Application;

import java.io.IOException;

public class DockerApp {
    public static void main(String[] args) {
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Hello world!");
        System.out.println("Hello world! 2");
    }
}