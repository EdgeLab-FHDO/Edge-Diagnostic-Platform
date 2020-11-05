import java.io.*;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.net.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import org.opencv.core.Mat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class OpenCVServer {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private DataInputStream in;
    private PrintWriter out;
    private DetectMarker detector;
    private int port;

    private String fileName= "read.png";

    private BufferedImage currentImage;

    private Thread serverThread;
    private Thread heartBeatThread;

    private static OpenCVServer instance = null;

    public OpenCVServer() {
    }

    public void startConnection() {
        try {
            serverSocket = new ServerSocket(port);
            clientSocket = serverSocket.accept();
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new DataInputStream(clientSocket.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // change name to something more appropriate
    public void standbyForConnection() {
        //if input is faulty, timeout and continue
        try {
            currentImage = ImageIO.read(ImageIO.createImageInputStream(clientSocket.getInputStream()));
            ImageIO.write(currentImage, "png", new File(fileName));
            Mat subject = ImageProcessor.getImageMat(fileName);

            detector = new DetectMarker();
            detector.detect(subject);
            String ids = OpenCVUtil.serializeMat(detector.getIds());
            String corners = OpenCVUtil.serializeMatList(detector.getCorners());
            String result = "";

            ObjectMapper mapper = new ObjectMapper();
            ObjectNode resultObject = mapper.createObjectNode();
            resultObject.put("ids", ids);
            resultObject.put("corners", corners);
            System.out.println(ids);
            System.out.println(corners);

            result = mapper.writeValueAsString(resultObject);

            out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
 
    public void stopConnection() {
        try {
            in.close();
            out.close();
            clientSocket.close();
            serverSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setup(String[] args) {
        String[] argument;
        port = 10200; //any port 10000 - 64999 otherwise as specified
        // make a rest request to the master to ask for server and port for server
        // do locally if no response
        try {
            for(int i=0; i<args.length; i++) {
                argument = args[i].split("=");
                switch(argument[0]) {
                    case "PORT":
                        this.port = port;
                        break;
                    default :
                        throw new IllegalArgumentException("Invaild argument");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static OpenCVServer getInstance() {
        if (instance == null) {
            instance = new OpenCVServer();
        }
        return instance;
    }
    
    public static void main(String[] args) {
        DetectMarker.initOpenCVSharedLibrary();
        OpenCVServer activeServer = OpenCVServer.getInstance();
        activeServer.setup(args);
        
        activeServer.serverThread = new Thread(new ServerRunner());
        activeServer.serverThread.start();
        HeartBeatRunner beatRunner = new HeartBeatRunner();
        beatRunner.url = "http://host.docker.internal:4567/node/register";
        beatRunner.body = "{" +
                "    \"id\": \"node2\"," +
                "    \"ipAddress\": \"172.19.0.1\"," +
                "    \"connected\": true," +
                "    \"port\": 10200" +
                "}";
        activeServer.heartBeatThread = new Thread(beatRunner);
        activeServer.heartBeatThread.start();
        
        try {
            activeServer.serverThread.join();
            activeServer.heartBeatThread.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}