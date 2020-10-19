import java.io.*;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.net.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import org.opencv.core.Mat;

public class OpenCVClient {
    private int utilizeServer = 0;
    private Socket clientSocket;
    private DataInputStream in;
    private DataOutputStream out;
    private DetectMarker detector;


    private String fileName = "singlemarkerssource.png";
    private String resultFileName = "detected.png";
    private String fileExtension = "png";
    private BufferedImage currentImage;

    private String ip = "server";
    private int port = 8080;

    private static OpenCVClient instance = null;

    public OpenCVClient() {
        detector = new DetectMarker(ImageProcessor.getImageMat(fileName));
    }

    public void startConnection() {
        try {
            clientSocket = new Socket(ip, port);
            out = new DataOutputStream(clientSocket.getOutputStream());
            in = new DataInputStream(clientSocket.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }   
    }

    public String sendImage() {
        String resp = "";
        try {
            currentImage = ImageIO.read(new File(fileName));
            ImageIO.write(currentImage, fileExtension, clientSocket.getOutputStream());
            currentImage = ImageIO.read(ImageIO.createImageInputStream(clientSocket.getInputStream()));
            ImageIO.write(currentImage, "png", new File("detected.png"));
            resp = "marker detected!";
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resp;
    }
 
    public void stopConnection() {
        try {
            in.close();
            out.close();
            clientSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getFileName() {
        return fileName;
    }

    public int useServer() {
        return utilizeServer;
    }

    public void setServerUtilization(int input) {
        utilizeServer = input;
    }

    private static OpenCVClient getInstance() {
        if (instance == null) {
            instance = new OpenCVClient();
        }
        return instance;
    }

    private void setup(String[] args) {
        String[] argument;
        try {
            for(int i=0; i<args.length; i++) {
                argument = args[i].split("=");
                switch(argument[0]) {
                    case "USE_SERVER":
                        setServerUtilization(Integer.parseInt(argument[1]));
                        break;
                    default :
                        throw new IllegalArgumentException("Invaild argument");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public Mat detectMarkerInServer() {
        // connect to server and detect marker
        String response = "";
        Mat result = new Mat();
        startConnection();
        // get response as string
        response = sendImage();
        // rebuild coreners and ids from response

        return result;
    }

    public void markerDetection() {
        Mat result;
        if(1 == utilizeServer) {
            detectMarkerInServer();
        } else {
            detector.detect();
        }
        result = detector.drawDetectedMarkers();
        ImageProcessor.writeImage(resultFileName, result);
    }

    public static void main(String[] args) {
        DetectMarker.initOpenCVSharedLibrary();
        OpenCVClient activeClient = OpenCVClient.getInstance();
        activeClient.setup(args);
        activeClient.markerDetection();
    }
}