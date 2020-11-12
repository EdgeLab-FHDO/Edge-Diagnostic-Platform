package Application.OpenCVServer;

import java.io.*;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.net.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import Application.Utilities.DetectMarker;
import Application.Utilities.HeartBeatRunner;
import Application.Utilities.ImageProcessor;
import org.opencv.core.Mat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class OpenCVServer {
    private Thread serverThread;
    private Thread heartBeatThread;

    public static void main(String[] args) {
        DetectMarker.initOpenCVSharedLibrary();
        OpenCVServer activeServer = new OpenCVServer();
        OpenCVServerOperator activeOperator = OpenCVServerOperator.getInstance();

        activeOperator.setupServerRunners(args);
        activeServer.serverThread = new Thread(activeOperator.serverRunner);
        activeServer.serverThread.start();
        activeServer.heartBeatThread = new Thread(activeOperator.beatRunner);
        activeServer.heartBeatThread.start();

        try {
            activeServer.serverThread.join();
            activeServer.heartBeatThread.join();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}