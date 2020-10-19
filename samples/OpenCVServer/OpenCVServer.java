import java.io.*;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.net.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import org.opencv.core.Mat;

public class OpenCVServer {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private DataInputStream in;
    private DataOutputStream out;
    private DetectMarker detector;

    private String fileName= "read.png";

    private BufferedImage currentImage;

    public OpenCVServer() {
    }

    public void start(int port) {
        try {
            serverSocket = new ServerSocket(port);
            clientSocket = serverSocket.accept();
            out = new DataOutputStream(clientSocket.getOutputStream());
            in = new DataInputStream(clientSocket.getInputStream());
            currentImage = ImageIO.read(ImageIO.createImageInputStream(clientSocket.getInputStream()));
            ImageIO.write(currentImage, "png", new File(fileName));
            detector = new DetectMarker(ImageProcessor.getImageMat(fileName));
            detector.detect();
            currentImage = ImageIO.read(new File("detected.png"));
            ImageIO.write(currentImage, "png", clientSocket.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
    public void stop() {
        try {
            in.close();
            out.close();
            clientSocket.close();
            serverSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        OpenCVServer server = new OpenCVServer();
        DetectMarker.initOpenCVSharedLibrary();

        server.start(8080);
    }
}