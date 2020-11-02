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

import java.util.concurrent.Semaphore;

public class OpenCVClient {
    private Socket clientSocket;
    private BufferedReader in;
    private DataOutputStream out;
    private DetectMarker detector;
    private ObjectMapper mapper;

    private String fileName = "singlemarkerssource.png";
    private String resultFileName = "detected.png";
    private String fileExtension = "png";
    private Mat subject;
    private BufferedImage currentImage;

    private Thread processingThread;
    private Thread masterCommunicationThread;
    private Thread heartBeatThread;
    public Semaphore ipLock;
    public Semaphore serverLock;

    //Server Communication Properties
    private int utilizeServer;
    private String ip;
    private int port;

    //Master Communication Properties
    private String masterUrl;

    private static OpenCVClient instance = null;

    public OpenCVClient() {
        mapper = new ObjectMapper();
        ipLock = new Semaphore(1);
        serverLock = new Semaphore(1);
        utilizeServer = 0;
    }

    public void startConnection() {
        //System.out.println("startConnection");
        try {
            ipLock.acquire();
            clientSocket = new Socket(ip, port);
            out = new DataOutputStream(clientSocket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (Exception e) {
            //e.printStackTrace();
        } finally {
            ipLock.release();
        }
    }

    public String sendImage() {
        //System.out.println("sendImage");
        String resp = "";
        SerializableMat receivedIds;
        Mat ids;
        try {
            //System.out.println("sendImage2");
            currentImage = ImageIO.read(new File(fileName));
            ImageIO.write(currentImage, fileExtension, clientSocket.getOutputStream());
                System.out.println("sendImage3");
                resp = in.readLine();
        } catch (Exception e) {
            //e.printStackTrace();
        }
        //System.out.println("sendImage4");

        return resp;
    }
 
    public void stopConnection() {
        //System.out.println("stopConnection");
        try {
            in.close();
            out.close();
            clientSocket.close();
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    public String getFileName() {
        //System.out.println("getFileName");

        return fileName;
    }

    public int useServer() {
        //System.out.println("useServer");
        
        return utilizeServer;
    }

    public void setServerUtilization(int input) {
        //System.out.println("setServerUtilization");
        try {
            //System.out.println("setServerUtilization2");
            serverLock.acquire();
            utilizeServer = input;
        } catch (Exception e) {
            //System.out.println("setServerUtilization3");
            //TODO: handle exception
            //e.printStackTrace();
        } finally {
            //System.out.println("setServerUtilization4");
            serverLock.release();
        }
        //System.out.println("setServerUtilization5");
        
    }

    public static OpenCVClient getInstance() {
        //System.out.println("getInstance");
        if (instance == null) {
            instance = new OpenCVClient();
        }
        return instance;
    }

    public void setup(String[] args) {
        //System.out.println("setup");

        String[] argument;
        // make a rest request to the master to ask for server and port for server
        // do locally if no response
        try {
            ipLock.acquire();
            for(int i=0; i<args.length; i++) {
                argument = args[i].split("=");
                switch(argument[0]) {
                    case "PORT":
                        port = Integer.parseInt(argument[1]);
                        System.out.println("port :" +port);
                        break;
                    case "IP":
                        ip = argument[1];
                        System.out.println("ip :" +ip);
                        break;
                    default :
                        throw new IllegalArgumentException("Invaild argument");
                }
            }
        } catch (Exception e) {
            //e.printStackTrace();
        } finally {
            ipLock.release();
        }
    }
    
    public void detectMarkerInServer() {
        System.out.println("detectMarkerInServer");
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
            //System.out.println(ids.dump());
            //System.out.println(corners.get(0).dump());
            System.out.println("Marker Drawn with remote informations");
        } catch (Exception e) {
            e.printStackTrace();
            detectMarkerInClient();
        }
        
        detector.setIds(ids);
        detector.setCorners(corners);
    }

    public void detectMarkerInClient() {
        //System.out.println("detectMarkerInClient");
        detector.detect(subject);
        System.out.println("Marker Drawn locally");
    }

    public void markerDetection() {
        //System.out.println("markerDetection");
        Mat result;
        Mat ids;
        List<Mat> corners;
        subject = ImageProcessor.getImageMat(fileName);
        detector = new DetectMarker();
        try {
        //System.out.println("markerDetection2");

            serverLock.acquire();
            if(1 == utilizeServer) {
                detectMarkerInServer();
            } else {
                detectMarkerInClient();
            }
        } catch (Exception e) {
            //TODO: handle exception
            //e.printStackTrace();
        } finally {
        //System.out.println("markerDetection3");
        serverLock.release();
        }
        
        result = detector.drawDetectedMarkers(subject);
        ImageProcessor.writeImage(resultFileName, result);
    }

    public static void main(String[] args) {
        DetectMarker.initOpenCVSharedLibrary();
        OpenCVClient activeClient = OpenCVClient.getInstance();
        activeClient.masterCommunicationThread = new Thread(new MasterCommunicationRunner(), "MasterCommunicationThread");
        activeClient.masterCommunicationThread.start();
        activeClient.processingThread = new Thread(new ProcessingRunner(), "ProcessingThread");
        activeClient.processingThread.start();
        activeClient.heartBeatThread = new Thread(new HeartBeatRunner(), "HeartBeatThread");
        activeClient.heartBeatThread.start();
        //System.out.println("threads started");
        
        try {
            activeClient.masterCommunicationThread.join();
            activeClient.processingThread.join();
            activeClient.heartBeatThread.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //System.out.println("exit");
    }
}