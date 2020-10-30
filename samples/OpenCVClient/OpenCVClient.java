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
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class OpenCVClient {
    private int utilizeServer = 0;
    private Socket clientSocket;
    private BufferedReader in;
    private DataOutputStream out;
    private DetectMarker detector;
    private ObjectMapper mapper;

    private String fileName = "singlemarkerssource.png";
    private String resultFileName = "detected.png";
    private String fileExtension = "png";
    private BufferedImage currentImage;

    private String ip = "server";
    private int port = 10200;

    private static OpenCVClient instance = null;

    public OpenCVClient() {
        mapper = new ObjectMapper();
    }

    public void startConnection() {
        try {
            clientSocket = new Socket(ip, port);
            out = new DataOutputStream(clientSocket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String sendImage() {
        String resp = "";
        SerializableMat receivedIds;
        Mat ids;
        try {
            currentImage = ImageIO.read(new File(fileName));
            ImageIO.write(currentImage, fileExtension, clientSocket.getOutputStream());
            resp = in.readLine();
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
        // make a rest request to the master to ask for server and port for server
        // do locally if no response
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

    public void detectMarkerInServer() {
        // connect to server and detect marker
        String response;
        JsonNode node;
        Mat ids = new Mat();
        List<Mat> corners = new ArrayList<Mat>();
        startConnection();
        response = sendImage().replace("\\\\", "");

        try {
            node = mapper.readTree(response);
            ids = OpenCVUtil.deserializeMat(node.get("ids").asText());
            corners = OpenCVUtil.deserializeMatList(node.get("corners").asText());
        } catch (Exception e) {
            e.printStackTrace();
        }

        detector.setIds(ids);
        detector.setCorners(corners);
    }

    public void markerDetection() {
        Mat result;
        Mat subject = ImageProcessor.getImageMat(fileName);
        Mat ids;
        List<Mat> corners;
        detector = new DetectMarker();
        if(1 == utilizeServer) {
            detectMarkerInServer();
        } else {
            detector.detect(subject);
        }
        result = detector.drawDetectedMarkers(subject);
        ImageProcessor.writeImage(resultFileName, result);
    }

    public static void main(String[] args) {
        DetectMarker.initOpenCVSharedLibrary();
        OpenCVClient activeClient = OpenCVClient.getInstance();
        /*loop
        thread 1
        while 1{
            setup
            while evaluation ok{
                heartbeat (new class)
                set utilize server to 1
                evaluation (new interface)
            }
            set utilizeServer to 0
        }
        thread 2
        while 1[
            run
        ]
        */
        activeClient.setup(args);
        activeClient.markerDetection();
    }
}