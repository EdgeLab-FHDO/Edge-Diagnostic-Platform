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
   public static void detect(String filename) {
      System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
      Dictionary dictionary = Aruco.getPredefinedDictionary(Aruco.DICT_6X6_250);
      Mat input = Imgcodecs.imread(filename);
      List<Mat> corners = new ArrayList();
      Mat ids = new Mat();

      Aruco.detectMarkers(input, dictionary, corners, ids);
      Mat output = input.clone();

      Aruco.drawDetectedMarkers(output, corners, ids);
      Imgcodecs.imwrite("detected.png", output);
   }
}