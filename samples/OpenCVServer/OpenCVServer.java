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

    private String fileName= "read.png";

    private BufferedImage currentImage;

    public OpenCVServer() {
    }

    public void start(int port) {
        try {
            serverSocket = new ServerSocket(port);
            clientSocket = serverSocket.accept();
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new DataInputStream(clientSocket.getInputStream());
            //if input is faulty, timeout and continue
            currentImage = ImageIO.read(ImageIO.createImageInputStream(clientSocket.getInputStream()));
            Mat subject = ImageProcessor.getImageMat(fileName);

            ImageIO.write(currentImage, "png", new File(fileName));
            detector = new DetectMarker();
            detector.detect(subject);
            String ids = OpenCVUtil.serializeMat(detector.getIds());
            String corners = OpenCVUtil.serializeMatList(detector.getCorners());
            String result = "";

            ObjectMapper mapper = new ObjectMapper();
            ObjectNode resultObject = mapper.createObjectNode();
            resultObject.put("ids", ids);
            resultObject.put("corners", corners);

            result = mapper.writeValueAsString(resultObject);

            out.println(result);
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
        DetectMarker.initOpenCVSharedLibrary();
        OpenCVServer server = new OpenCVServer();
        int port = 10200; // take port as argument
        server.start(port);
    }
}