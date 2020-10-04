import java.io.*;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.net.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class OpenCVClient {
    public int utilizeServer = 0;
    private Socket clientSocket;
    private DataInputStream in;
    private DataOutputStream out;

    private String fileName = "singlemarkerssource.png";
    private String fileExtension = "png";
    private BufferedImage currentImage;

    private String ip = "server";
    private int port = 8080;

    private static OpenCVClient instance = null;

    public OpenCVClient() {}

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

    public static void main(String[] args) {
        OpenCVClient.getInstance().setup(args);
        if(1 == OpenCVClient.getInstance().useServer()) {
            //connect to server and detect marker
            System.out.println("Under Development");
            OpenCVClient.getInstance().startConnection();
            System.out.println(OpenCVClient.getInstance().sendImage());
        } else {
            DetectMarker.detect(OpenCVClient.getInstance().getFileName());
        }
    }
}