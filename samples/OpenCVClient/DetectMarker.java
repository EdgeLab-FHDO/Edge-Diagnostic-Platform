import org.opencv.core.*;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.aruco.*;
import org.opencv.aruco.Dictionary.*;

import java.io.File;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class DetectMarker {
   public static void main(String[] args) {
                System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
                Dictionary dictionary = Aruco.getPredefinedDictionary(Aruco.DICT_6X6_250);
                Mat input = Imgcodecs.imread("marker.png");
                List<Mat> corners = new ArrayList();
                Mat ids = new Mat();

                Aruco.detectMarkers(input, dictionary, corners, ids);
                System.out.println("corners: " + corners);
                System.out.println("ids: " + ids.dump());
                Mat output = input.clone();

                Aruco.drawDetectedMarkers(output, corners, ids);
                Imgcodecs.imwrite("detected.png", output);
   }
}